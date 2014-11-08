package jary.drools.factory.wrapper;

import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.springframework.stereotype.Component;

/**
 * wrapper class to allow static mock testing of classes using knowledge builders
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
@Component
public class KnowledgeBuilderFactoryWrapper {

    /**
     * return new knowledge builder from factory
     *
     * @return knowledge builder
     */
    public KnowledgeBuilder newKnowledgeBuilder() {
        return KnowledgeBuilderFactory.newKnowledgeBuilder();
    }
}
