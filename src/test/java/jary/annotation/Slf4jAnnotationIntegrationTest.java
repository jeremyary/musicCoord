package jary.annotation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * test class for annotation-based logger injection
 * {@link jary.annotation.Slf4j}
 * {@link jary.annotation.Slf4jInjector}
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml", "classpath:spring-properties.xml"})
public class Slf4jAnnotationIntegrationTest {

    @Slf4j
    private Logger log;

    @Test
    public void testAnnotationInjection() {

        Assert.assertNotNull(log);
        assert (log instanceof Logger);
        log.debug("it works!");
    }
}