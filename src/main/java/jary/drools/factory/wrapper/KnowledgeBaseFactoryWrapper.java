package jary.drools.factory.wrapper;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.springframework.stereotype.Component;

/**
 * wrapper class to allow static mock testing of classes using knowledge base factory
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
@Component
public class KnowledgeBaseFactoryWrapper {

    /**
     * return new knowledge builder from factory
     *
     * @return knowledge builder
     */
    public KnowledgeBase newKnowledgeBase() {
        return KnowledgeBaseFactory.newKnowledgeBase();
    }
}
