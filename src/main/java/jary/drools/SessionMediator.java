package jary.drools;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Lazy
public class SessionMediator {

    /**
     * rule delegate to hand off to each session builder instance
     */
    @Autowired
    private RuleDelegate delegate;

    /**
     * rule session
     */
    private StatefulKnowledgeSession session;

    /**
     * insert object into session working memory
     *
     * @param fact object to be inserted
     * @return factHandle reference of object in working memory
     */
    public FactHandle insert(Object fact) {
        return session.insert(fact);
    }

    /**
     * trigger session agenda firing
     *
     * @return number of rules fired
     */
    public Integer fire() {

        // provide global delegate
        session.setGlobal("delegate", delegate);

        try {
            return session.fireAllRules();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            session.dispose();
        }
        return -1;
    }

    /**
     * setter for session
     *
     * @param session
     */
    public void setSession(StatefulKnowledgeSession session) {
        this.session = session;
    }

    /**
     * setter for delegate for testing purposes
     *
     * @param delegate
     */
    public void setDelegate(RuleDelegate delegate) {
        this.delegate = delegate;
    }
}
