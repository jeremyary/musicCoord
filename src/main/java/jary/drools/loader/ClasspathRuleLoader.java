package jary.drools.loader;

import jary.drools.factory.wrapper.KnowledgeBuilderFactoryWrapper;
import jary.drools.factory.wrapper.ResourceFactoryWrapper;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.ResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * rule loader that will take the specified rule files found within the classpath and load them into the returned
 * knowledge builder
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
@Component
public class ClasspathRuleLoader implements RuleLoader {

    /**
     * factory wrapper supplying knowledge builder
     */
    @Autowired
    private KnowledgeBuilderFactoryWrapper builderFactoryWrapper;

    /**
     * factory wrapped supplying resources
     */
    @Autowired
    private ResourceFactoryWrapper resourceFactoryWrapper;


    /**
     * get rules off classpath, populate a new builder, and return it
     *
     * @return knowledge builder with rules added in
     */
    public KnowledgeBuilder load(String resource) {

        KnowledgeBuilder builder = builderFactoryWrapper.newKnowledgeBuilder();

        builder.add(resourceFactoryWrapper.newClassPathResource(resource), ResourceType.DRL);
        if (builder.hasErrors()) {
            throw new RuntimeException(builder.getErrors().toString());
        }
        return builder;
    }

    /**
     * setter for builderFactoryWrapper for testing purposes
     *
     * @param wrapper
     */
    public void setBuilderFactoryWrapper(KnowledgeBuilderFactoryWrapper wrapper) {
        this.builderFactoryWrapper = wrapper;
    }

    /**
     * setter for resourceFactoryWrapper for testing purposes
     *
     * @param wrapper
     */
    public void setResourceFactoryWrapper(ResourceFactoryWrapper wrapper) {
        this.resourceFactoryWrapper = wrapper;
    }
}