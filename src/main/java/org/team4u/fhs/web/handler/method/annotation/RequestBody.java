package org.team4u.fhs.web.handler.method.annotation;

import org.team4u.fhs.web.handler.method.support.DefaultRequestBodyArgumentDecoder;
import org.team4u.fhs.web.handler.method.support.RequestBodyArgumentDecoder;

import java.lang.annotation.*;

/**
 * Annotation indicating a method parameter should be bound to the body of the web request.
 *
 * @author Jay Wu
 * @see RequestHeader
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestBody {

    Class<? extends RequestBodyArgumentDecoder> value() default DefaultRequestBodyArgumentDecoder.class;
}