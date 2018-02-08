package org.team4u.fhs.web.handler.method.annotation;

import java.lang.annotation.*;

/**
 * Annotation which indicates that a method parameter should be bound to a web
 * request parameter.
 *
 * @author Jay Wu
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {

    /**
     * The name of the request parameter to bind to.
     */
    String value();
}