package jary.amqp;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * manage amqp connectivity and queue collection
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
@Component
public class AmqpMediator {

    /** used for purging the aml request queue after testing to prevent multiples from existing on error */
    @Autowired
    private AmqpAdmin amqpAdmin;

    /** status message queue where we'll shove stuff to keep from lagging on the API end */
    @Value("${rabbit.queues.twitter.stream.status}")
    private String statusQueue;

    /** delete message queue */
    @Value("${rabbit.queues.twitter.stream.delete}")
    private String deleteQueue;

    /** geo information scrum message queue */
    @Value("${rabbit.queues.twitter.stream.scrub}")
    private String scrubQueue;

    /** collection of message queue names */
    private List<String> queues = new LinkedList<String>();

    /**
     * flush queues
     */
    public void flush() {
        for (String queue : getQueues()) {
            amqpAdmin.purgeQueue(queue, false);
        }
    }

    /**
     * setter for amqpAdmin
     *
     * @param amqpAdmin
     */
    public void setAmqpAdmin(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    /**
     * getter for amqpAdmin, allows direct queue management
     *
     * @return admin instance
     */
    public AmqpAdmin getAmqpAdmin() {
        return amqpAdmin;
    }

    /**
     * getter for queues
     *
     * @return list of queue names we'll be using
     */
    public List<String> getQueues() {
        if (queues.size() == 0) {
            queues.add(statusQueue);
            queues.add(deleteQueue);
            queues.add(scrubQueue);
        }
        return queues;
    }
}