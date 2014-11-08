package jary.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * Injector for adding logging to classes via annotation {@link Slf4j}
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
@Component
public class Slf4jInjector implements BeanPostProcessor {

    /**
     * empty post-process, not needed for this impl
     *
     * @param bean
     * @param beanName
     * @return
     * @throws org.springframework.beans.BeansException
     */
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * post-process interaction w/Spring, map logger bean to annotated field
     *
     * @param bean bean containing field annotated with log property
     * @param beanName name of bean
     * @return bean with logger field set up
     * @throws org.springframework.beans.BeansException
     */
    public Object postProcessBeforeInitialization(final Object bean, String beanName) throws BeansException {

        ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {

            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {

                // if private, provide access
                ReflectionUtils.makeAccessible(field);
                if (field.getAnnotation(Slf4j.class) != null) {
                    Logger log = LoggerFactory.getLogger(bean.getClass());
                    field.set(bean, log);
                }
            }
        });
        return bean;
    }
}