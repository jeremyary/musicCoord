package jary.drools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jary.annotation.Slf4j;
import jary.drools.loader.RuleLoader;
import jary.drools.model.*;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Delegate responsible for spinning off async tasks spawned from rule sessions
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
@Component
public class RuleDelegate {

    @Slf4j
    private Logger log;

    @Autowired
    @Qualifier("ruleEngineTaskExecutor")
    ThreadPoolTaskExecutor executor;

    @Autowired
    private RuleLoader ruleLoader;

    public final static String MUSIC_RULES = "drools/MusicRules.drl";
    protected StatefulKnowledgeSession session;

    protected JedisPoolConfig poolConfig;
    protected JedisPool jedisPool;
    protected Jedis subscribeBeats;
    protected Jedis subscribeAnalysis;
    protected Jedis publisher;
    protected JedisPubSub beatSub;
    protected JedisPubSub analysisSub;
    protected ArrayList<String> messageContainer = new ArrayList<>();
    protected CountDownLatch messageReceivedLatch = new CountDownLatch(1);
    protected FactHandle analysisHandle;

    @PostConstruct
    public void init() {

        poolConfig = new JedisPoolConfig();
        jedisPool = new JedisPool(poolConfig, "172.31.253.53", 6379, 0);
//        jedisPool = new JedisPool(poolConfig, "localhost", 6379, 0);
        subscribeBeats = jedisPool.getResource();
        subscribeAnalysis = jedisPool.getResource();
        publisher = jedisPool.getResource();

        KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();

        knowledgeBase.addKnowledgePackages(ruleLoader.load(MUSIC_RULES).getKnowledgePackages());
        session = knowledgeBase.newStatefulKnowledgeSession();
        session.setGlobal("delegate", this);
//        session.insert(new AdjustTestState()); // TESTING PURPOSES ONLY
        session.insert(new DanceTrendUp());
        session.insert(new DanceTrendDown());
        session.insert(new Interval(false));

        executor.execute(() -> {
            try {
                session.fireUntilHalt();
            } catch (Exception e) {
                log.error(">>> OH NOES RULES SESSION - " + e.getMessage());
            }
        });

        executor.execute(() -> {
            try {
                setupSubscribers();
                messageReceivedLatch.await();

            } catch (Exception e) {
                log.error(">>> OH NOES Sub - " + e.getMessage());
            }
        });
    }

    public void calculateAdjustment(final SongAnalysisEvent analysisEvent, final List<DanceEvent> events,
                                    final Integer interval) {

        if (analysisEvent.getTempo().isNaN() || analysisEvent.getTempo().isInfinite() || analysisEvent.getTempo() == 0) {
            return;
        }


        if (events.size() == 0) {
            log.info("no events over interval, ignoring...");
            return;
        }

        Double musicBeats = ((analysisEvent.getTempo() / 60) /  4);
        Double danceBeats = (events.size() / interval) * 1.0;

        if (danceBeats.isNaN() || danceBeats.isInfinite() || danceBeats == 0)
            return;

        log.info(">>> measure comparison - dancer: " + danceBeats + ", music: " + musicBeats);
        log.info(">>> dancer events captured: " + events.size());

        if (musicBeats > danceBeats) {

            if (musicBeats > danceBeats * 2)
                musicBeats /= 2;

            Double adjustment = (danceBeats / musicBeats < 0.8) ? 0.8 : danceBeats / musicBeats;
            log.info("issuing slow down adjustment of " + adjustment);
            dispatchAdjustment(new SongAdjustEvent("foo", System.currentTimeMillis(), 0L, adjustment));

            SongAnalysisEvent analysisFromEngine = (SongAnalysisEvent) session.getObject(analysisHandle);
            analysisFromEngine.setTempo(analysisEvent.getTempo() * (danceBeats / musicBeats));
            session.update(analysisHandle, analysisFromEngine);

        } else if (musicBeats < danceBeats) {

            if (danceBeats > musicBeats * 2)
                danceBeats /= 2;

            Double adjustment = (danceBeats / musicBeats > 1.2) ? 1.2 : danceBeats / musicBeats;
            log.info("issuing speed up adjustment of " + adjustment);
            dispatchAdjustment(new SongAdjustEvent("foo", System.currentTimeMillis(), 0L, adjustment));

            SongAnalysisEvent analysisFromEngine = (SongAnalysisEvent) session.getObject(analysisHandle);
            analysisFromEngine.setTempo(analysisEvent.getTempo() * (danceBeats / musicBeats));
            session.update(analysisHandle, analysisFromEngine);

        } else {
            log.info("difference in bpm negligible");
        }

        for (DanceEvent event : events) {
            session.retract(session.getFactHandle(event));
        }
    }

    public void dispatchAdjustment(final SongAdjustEvent adjustEvent) {

        try {
            ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
            publisher.publish("song-adjust", objectWriter.writeValueAsString(adjustEvent));

        } catch (Exception ex) {
            log.error("ERROR IN FORMING ADJUSTMENT JSON: " + ex.toString());
        }
    }

    private void setupSubscribers() {
        beatSub = new JedisPubSub() {
            @Override
            public void onUnsubscribe(String channel, int subscribedChannels) {
            }

            @Override
            public void onSubscribe(String channel, int subscribedChannels) {
            }

            @Override
            public void onPUnsubscribe(String pattern, int subscribedChannels) {
            }

            @Override
            public void onPSubscribe(String pattern, int subscribedChannels) {
            }

            @Override
            public void onPMessage(String pattern, String channel, String message) {
            }

            @Override
            public void onMessage(String channel, String message) {
                messageContainer.add(message);

                try {
                    log.info("ADDING DANCE EVENT");
                    session.insert(new ObjectMapper().readValue(message, DanceEvent.class));
                } catch (Exception ex) {
                    log.error("ERROR IN CONVERSION OF DANCE EVENT TO POJO: " + ex.toString());
                    ex.printStackTrace();
                }
                messageReceivedLatch.countDown();

            }
        };

        analysisSub = new JedisPubSub() {
            @Override
            public void onUnsubscribe(String channel, int subscribedChannels) {
            }

            @Override
            public void onSubscribe(String channel, int subscribedChannels) {
            }

            @Override
            public void onPUnsubscribe(String pattern, int subscribedChannels) {
            }

            @Override
            public void onPSubscribe(String pattern, int subscribedChannels) {
            }

            @Override
            public void onPMessage(String pattern, String channel, String message) {
            }

            @Override
            public void onMessage(String channel, String message) {
                messageContainer.add(message);

                try {

                    Collection<Object> objects = session.getObjects();
                    for (Object object : objects) {
                        if (object instanceof DanceEvent) {
                            session.retract(session.getFactHandle(object));
                        }
                    }

                    analysisHandle = session.insert(new ObjectMapper().readValue(message, SongAnalysisEvent.class));
                } catch (Exception ex) {
                    log.error("ERROR IN CONVERSION OF DANCE EVENT TO POJO: " + ex.toString());
                    ex.printStackTrace();
                }
                messageReceivedLatch.countDown();

            }
        };

        executor.execute(() -> {
            try {
                log.debug("STARTING SUBSCRIBE");

                subscribeBeats.subscribe(beatSub, "dance-beat");
                subscribeBeats.quit();

            } catch (Exception e) {
                log.error(">>> OH NOES Sub - " + e.toString());
                e.printStackTrace();
            }
        });

        executor.execute(() -> {
            try {
                log.debug("STARTING SUBSCRIBE");

                subscribeAnalysis.subscribe(analysisSub, "song-analysis");
                subscribeAnalysis.quit();

            } catch (Exception e) {
                log.error(">>> OH NOES Sub - " + e.toString());
                e.printStackTrace();
            }
        });
    }

}