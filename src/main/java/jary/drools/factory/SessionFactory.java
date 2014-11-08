package jary.drools.factory;

import jary.drools.SessionMediator;
import jary.drools.factory.wrapper.KnowledgeBaseFactoryWrapper;
import jary.drools.loader.RuleLoader;
import org.drools.KnowledgeBase;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Factory to provide a populated rule session
 * see bean definition in spring.xml due to lack of annotation support for lookup-method
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
public abstract class SessionFactory {

    /**
     * wrapper for factory providing knowledge bases
     */
    @Autowired
    private KnowledgeBaseFactoryWrapper wrapper;

    /**
     * classpath rule resource loaded
     */
    @Autowired
    private RuleLoader ruleLoader;

    /**
     * test-accessible hashtag rule resource path
     */
    public final static String HASHTAG_RULES = "";

    /**
     * test-accessible user rule resource path
     */
    public final static String USER_RULES = "drools/UserPatterns.drl";

    /**
     * build session from contructed object; ready to fire
     *
     * @return
     */
    public SessionMediator build() {

        KnowledgeBase knowledgeBase = getWrapper().newKnowledgeBase();

        knowledgeBase.addKnowledgePackages(getRuleLoader().load(USER_RULES).getKnowledgePackages());
        SessionMediator mediator = getSessionMediator();
        mediator.setSession(knowledgeBase.newStatefulKnowledgeSession());

        return mediator;
    }

    /**
     * getter for wrapper for testing purposes
     *
     * @return wrapper
     */
    public KnowledgeBaseFactoryWrapper getWrapper() {
        return this.wrapper;
    }

    /**
     * getter for ruleLoader for testing purposes
     *
     * @return ruleLoader
     */
    public RuleLoader getRuleLoader() {
        return this.ruleLoader;
    }

    /**
     * setter for wrapper for testing purposes
     *
     * @param wrapper
     */
    public void setWrapper(KnowledgeBaseFactoryWrapper wrapper) {
        this.wrapper = wrapper;
    }

    /**
     * setter for ruleLoader for testing purposes
     *
     * @param loader
     */
    public void setRuleLoader(RuleLoader loader) {
        this.ruleLoader = loader;
    }

    /**
     * lookup-method for mediator bean prototype injection
     *
     * @return sessionMediator for rule session instance
     */
    protected abstract SessionMediator getSessionMediator();
}