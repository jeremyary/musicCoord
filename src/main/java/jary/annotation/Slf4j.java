package jary.annotation;


import java.lang.annotation.*;

/**
 * provide an annotation-based injection for slf4j logging
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Slf4j {
    // for Slf4j injection
}