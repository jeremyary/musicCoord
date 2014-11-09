package jary.drools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jary.annotation.Slf4j;
import jary.drools.loader.RuleLoader;
import jary.drools.model.*;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
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

    @Value("${url.analysis.destination}")
    private String destination;

    @Autowired
    @Qualifier("ruleEngineTaskExecutor")
    ThreadPoolTaskExecutor executor;

    @Autowired
    private RuleLoader ruleLoader;

    public final static String MUSIC_RULES = "drools/MusicRules.drl";
    protected StatefulKnowledgeSession session;

    protected JedisPoolConfig poolConfig;
    protected JedisPool jedisPool;
    protected Jedis subscriber;
    protected Jedis publisher;
    protected JedisPubSub jedisPubSub;
    protected ArrayList<String> messageContainer = new ArrayList<>();
    protected CountDownLatch messageReceivedLatch = new CountDownLatch(1);

    @PostConstruct
    public void init() {

        poolConfig = new JedisPoolConfig();
        jedisPool = new JedisPool(poolConfig, "172.31.253.53", 6379, 0);
//        jedisPool = new JedisPool(poolConfig, "localhost", 6379, 0);
        subscriber = jedisPool.getResource();
        publisher = jedisPool.getResource();

        KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();

        knowledgeBase.addKnowledgePackages(ruleLoader.load(MUSIC_RULES).getKnowledgePackages());
        session = knowledgeBase.newStatefulKnowledgeSession();
        session.setGlobal("delegate", this);
        session.insert(new Interval(1000));
        session.insert(new AdjustTestState());

        executor.execute(() -> {
            try {
                session.fireUntilHalt();
            } catch (Exception e) {
                log.error(">>> OH NOES RULES SESSION - " + e.getMessage());
            }
        });

        executor.execute(() -> {
            try {
                setupSubscriber();
                messageReceivedLatch.await();

            } catch (Exception e) {
                log.error(">>> OH NOES Sub - " + e.getMessage());
            }
        });
    }

    public void dispatchAdjustment(final SongAdjustEvent adjustEvent) {

        try {
            ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
            publisher.publish("song-analysis", objectWriter.writeValueAsString(adjustEvent));

        } catch (Exception ex) {
            log.error("ERROR IN FORMING ADJUSTMENT JSON: " + ex.toString());
        }
    }

    private void setupSubscriber() {
        jedisPubSub = new JedisPubSub() {
            @Override
            public void onUnsubscribe(String channel, int subscribedChannels) {
                log.debug("onUnsubscribe");
            }

            @Override
            public void onSubscribe(String channel, int subscribedChannels) {
                log.debug("onSubscribe");
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
                log.info("Message received: " + message);

                try {
                    if (channel.equalsIgnoreCase("dance-beat"))
                        session.insert(new ObjectMapper().readValue(message, DanceEvent.class));

                    else if (channel.equalsIgnoreCase("song-analysis"))
                        session.insert(new ObjectMapper().readValue(message, SongAnalysisEvent.class));

                    else
                        throw new IllegalArgumentException("UNKNOWN REC'D CHANNEL - " + channel);


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
                Jedis jedis = jedisPool.getResource();
                jedis.subscribe(jedisPubSub, "dance-beat");
                jedis.subscribe(jedisPubSub, "song-analysis");
                jedis.quit();
            } catch (Exception e) {
                log.error(">>> OH NOES Sub - " + e.toString());
                e.printStackTrace();
            }
        });
    }

}