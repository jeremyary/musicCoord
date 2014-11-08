package jary.drools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jary.annotation.Slf4j;
import jary.drools.model.SongAdjustEvent;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    public void dispatchAdjustment(final SongAdjustEvent adjustEvent) {

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(destination);

        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

        try {
            httpPost.setEntity(new StringEntity(objectWriter.writeValueAsString(adjustEvent),
                    ContentType.create("application/json")));
        } catch (Exception ex) {
            log.error("ERROR IN FORMING ADJUSTMENT JSON: " + ex.toString());
        }

        try {
            httpClient.execute(httpPost);
        } catch (Exception ex) {
            log.error("ERROR ENCOUNTERED IN DISPATCHING ADJUSTMENT: " + ex.toString());
        }
    }
}