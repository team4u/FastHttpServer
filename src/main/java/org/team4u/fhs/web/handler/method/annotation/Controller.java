package org.team4u.fhs.web.handler.method.annotation;

import java.lang.annotation.*;

/**
 * Indicates that an annotated class is a "Controller" (e.g. a web controller).
 *
 * @author Jay Wu
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {

    /**
     * The value may indicate a suggestion for a logical component name,
     * to be turned into a Spring bean in case of an autodetected component.
     *
     * @return the suggested component name, if any
     */
    String value() default "";
}