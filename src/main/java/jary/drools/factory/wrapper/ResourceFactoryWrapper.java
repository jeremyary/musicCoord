package jary.drools.factory.wrapper;

import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.springframework.stereotype.Component;

/**
 * wrapper class to allow static mock testing of classes using resource factory
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
@Component
public class ResourceFactoryWrapper {

    public Resource newClassPathResource(String resource) {
        return ResourceFactory.newClassPathResource(resource);
    }
}
