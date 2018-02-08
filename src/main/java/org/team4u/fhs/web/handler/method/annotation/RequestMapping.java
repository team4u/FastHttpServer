package org.team4u.fhs.web.handler.method.annotation;

import org.team4u.fhs.server.HttpMethod;

import java.lang.annotation.*;

/**
 * @author Jay Wu
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

    /**
     * Assign a name to this mapping.
     */
    String value() default "";

    /**
     * The HTTP request methods to map to, narrowing the primary mapping:
     * GET, POST, HEAD, OPTIONS, PUT, PATCH, DELETE, TRACE.
     * <p><b>Supported at the type level as well as at the method level!</b>
     * When used at the type level, all method-level mappings inherit
     * this HTTP method restriction (i.e. the type-level restriction
     * gets checked before the handler method is even resolved).
     */
    HttpMethod[] method() default {};

    /**
     * The consumable media types of the mapped request, narrowing the primary mapping.
     * <p>The format is a single media type or a sequence of media types,
     * with a request only mapped if the {@code Content-Type} matches one of these media types.
     * Examples:
     * <pre class="code">
     * consumes = "text/plain"
     * consumes = {"text/plain", "application/*"}
     * </pre>
     * Expressions can be negated by using the "!" operator, as in "!text/plain", which matches
     * all requests with a {@code Content-Type} other than "text/plain".
     * <p><b>Supported at the type level as well as at the method level!</b>
     * When used at the type level, all method-level mappings override
     * this consumes restriction.
     */
    String[] consumes() default {};

    /**
     * The producible media types of the mapped request, narrowing the primary mapping.
     * <p>The format is a single media type or a sequence of media types,
     * with a request only mapped if the {@code Accept} matches one of these media types.
     * Examples:
     * <pre class="code">
     * produces = "text/plain"
     * produces = {"text/plain", "application/*"}
     * produces = "application/json; charset=UTF-8"
     * </pre>
     * <p>It affects the actual content type written, for example to produce a JSON response
     * with UTF-8 encoding, {@code "application/json; charset=UTF-8"} should be used.
     * <p>Expressions can be negated by using the "!" operator, as in "!text/plain", which matches
     * all requests with a {@code Accept} other than "text/plain".
     * <p><b>Supported at the type level as well as at the method level!</b>
     * When used at the type level, all method-level mappings override
     * this produces restriction.
     */
    String[] produces() default {};
}