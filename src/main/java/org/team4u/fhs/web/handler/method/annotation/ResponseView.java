package org.team4u.fhs.web.handler.method.annotation;

import java.lang.annotation.*;

/**
 * @author Jay Wu
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseView {

    String success() default "";

    String error() default "";
}