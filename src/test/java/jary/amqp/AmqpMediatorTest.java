package jary.amqp;

import org.junit.Before;
import org.junit.Test;
import org.springframework.amqp.core.AmqpAdmin;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * tests for class {@link jary.amqp.AmqpMediator}
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
public class AmqpMediatorTest {

    private AmqpMediator mediator;

    @Before
    public void setUp() {
        mediator = new AmqpMediator();
    }

    @Test
    public void testFlush() {

        AmqpAdmin mockAdmin = mock(AmqpAdmin.class);
        mediator.setAmqpAdmin(mockAdmin);

        mediator.getQueues().add("foo");

        mediator.flush();

        verify(mockAdmin).purgeQueue("foo", false);
    }
}