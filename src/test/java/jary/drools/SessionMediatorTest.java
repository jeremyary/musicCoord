package jary.drools;

import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * tests for class {@link jary.drools.SessionMediator}
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
public class SessionMediatorTest {

    SessionMediator mediator;

    @Before
    public void setUp() {
        this.mediator = new SessionMediator();
    }

    @Test
    public void testInsert() {

        Object fact = new Object();
        StatefulKnowledgeSession session = mock(StatefulKnowledgeSession.class);
        mediator.setSession(session);

        mediator.insert(fact);

        verify(session).insert(fact);
    }

    @Test
    public void testFireNoException() {

        RuleDelegate delegate = mock(RuleDelegate.class);
        mediator.setDelegate(delegate);

        StatefulKnowledgeSession session = mock(StatefulKnowledgeSession.class);
        when(session.fireAllRules()).thenReturn(1);
        mediator.setSession(session);

        mediator.fire();

        verify(session).setGlobal("delegate", delegate);
        verify(session).dispose();
    }
}