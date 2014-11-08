package jary.drools.factory;

import jary.drools.SessionMediator;
import jary.drools.factory.wrapper.KnowledgeBaseFactoryWrapper;
import jary.drools.loader.RuleLoader;
import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.definition.KnowledgePackage;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.mockito.Mockito.*;

/**
* tests for class {@link SessionFactory}
*
* @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
*/
public class SessionFactoryTest {

    private SessionFactory sessionFactory;

    @Before
    public void setUp() {
        sessionFactory = mock(SessionFactory.class);
        SessionMediator mediator = mock(SessionMediator.class);
        when(sessionFactory.getSessionMediator()).thenReturn(mediator);
    }

    @Test
    public void testBuildForHashtag() {

        StatefulKnowledgeSession session = mock(StatefulKnowledgeSession.class);

        KnowledgeBase knowledgeBase = mock(KnowledgeBase.class);
        when(knowledgeBase.newStatefulKnowledgeSession()).thenReturn(session);

        KnowledgeBaseFactoryWrapper wrapper = mock(KnowledgeBaseFactoryWrapper.class);
        when(wrapper.newKnowledgeBase()).thenReturn(knowledgeBase);
        when(sessionFactory.getWrapper()).thenReturn(wrapper);

        Collection<KnowledgePackage> collection = mock(Collection.class);
        KnowledgeBuilder builder = mock(KnowledgeBuilder.class);
        when(builder.getKnowledgePackages()).thenReturn(collection);

        RuleLoader loader = mock(RuleLoader.class);
        when(loader.load(SessionFactory.HASHTAG_RULES)).thenReturn(builder);
        when(sessionFactory.getRuleLoader()).thenReturn(loader);

        doCallRealMethod().when(sessionFactory).build();
        SessionMediator result = sessionFactory.build();

        verify(wrapper).newKnowledgeBase();
        verify(knowledgeBase).addKnowledgePackages(collection);
        verify(loader).load(SessionFactory.HASHTAG_RULES);
        verify(builder).getKnowledgePackages();
        verify(result).setSession(session);

        assert result != null;
        assert result instanceof SessionMediator;
    }

    @Test
    public void testBuildForUser() {

        StatefulKnowledgeSession session = mock(StatefulKnowledgeSession.class);

        KnowledgeBase knowledgeBase = mock(KnowledgeBase.class);
        when(knowledgeBase.newStatefulKnowledgeSession()).thenReturn(session);

        KnowledgeBaseFactoryWrapper wrapper = mock(KnowledgeBaseFactoryWrapper.class);
        when(wrapper.newKnowledgeBase()).thenReturn(knowledgeBase);
        when(sessionFactory.getWrapper()).thenReturn(wrapper);

        Collection<KnowledgePackage> collection = mock(Collection.class);
        KnowledgeBuilder builder = mock(KnowledgeBuilder.class);
        when(builder.getKnowledgePackages()).thenReturn(collection);

        RuleLoader loader = mock(RuleLoader.class);
        when(loader.load(SessionFactory.USER_RULES)).thenReturn(builder);
        when(sessionFactory.getRuleLoader()).thenReturn(loader);

        doCallRealMethod().when(sessionFactory).build();
        SessionMediator result = sessionFactory.build();

        verify(wrapper).newKnowledgeBase();
        verify(knowledgeBase).addKnowledgePackages(collection);
        verify(loader).load(SessionFactory.USER_RULES);
        verify(builder).getKnowledgePackages();
        verify(result).setSession(session);

        assert result != null;
        assert result instanceof SessionMediator;
    }
}