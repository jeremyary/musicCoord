package jary.amqp;

import jary.annotation.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

/**
 * Error handler for amqp transactions
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
@Component
public class AmqpErrorHandler implements ErrorHandler {

    @Slf4j
    private Logger log;

    /**
     * log out the error incurred so that it's not lost in visibility
     *
     * @param t throwable error to handle
     */
    @Override
    public void handleError(Throwable t) {
        log.error("messaging error", t);
    }
}