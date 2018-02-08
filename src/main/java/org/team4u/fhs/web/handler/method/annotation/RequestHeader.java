package org.team4u.fhs.web.handler.method.annotation;

import java.lang.annotation.*;

/**
 * Annotation which indicates that a method parameter should be bound to a web request header.
 *
 * @author Jay Wu
 * @see RequestMapping
 * @see RequestParam
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestHeader {

    /**
     * The name of the request header to bind to.
     */
    String value();
}
