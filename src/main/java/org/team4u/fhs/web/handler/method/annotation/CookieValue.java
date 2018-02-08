package org.team4u.fhs.web.handler.method.annotation;

import java.lang.annotation.*;

/**
 * Annotation which indicates that a method parameter should be bound to an HTTP cookie.
 *
 * @author Jay Wu
 * @see RequestMapping
 * @see RequestParam
 * @see RequestHeader
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CookieValue {

    /**
     * The name of the cookie to bind to.
     */
    String value();
}