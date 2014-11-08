package jary.drools.loader;

import jary.drools.factory.wrapper.KnowledgeBuilderFactoryWrapper;
import jary.drools.factory.wrapper.ResourceFactoryWrapper;
import org.drools.builder.KnowledgeBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * test for class {@link jary.drools.loader.ClasspathRuleLoader}
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
public class ClasspathRuleLoaderTest {

    private ClasspathRuleLoader loader;

    @Before
    public void setUp() {
        loader = new ClasspathRuleLoader();
    }

    @Test
    public void testLoad() {

        ResourceFactoryWrapper resourceFactoryWrapper = mock(ResourceFactoryWrapper.class);
        when(resourceFactoryWrapper.newClassPathResource("foo")).thenReturn(null);
        loader.setResourceFactoryWrapper(resourceFactoryWrapper);

        KnowledgeBuilder builder = mock(KnowledgeBuilder.class);
        when(builder.hasErrors()).thenReturn(false);

        KnowledgeBuilderFactoryWrapper builderFactoryWrapper = mock(KnowledgeBuilderFactoryWrapper.class);
        when(builderFactoryWrapper.newKnowledgeBuilder()).thenReturn(builder);
        loader.setBuilderFactoryWrapper(builderFactoryWrapper);

        KnowledgeBuilder returnedBuilder = loader.load("foo");

        verify(resourceFactoryWrapper).newClassPathResource("foo");
        verify(builder).hasErrors();
        verify(builderFactoryWrapper).newKnowledgeBuilder();
        assert returnedBuilder == builder;
    }
}
