package org.team4u.fhs.web.handler.method.support;


import org.team4u.fhs.web.RoutingContext;
import org.team4u.fhs.web.handler.method.MethodParameter;

/**
 * Strategy interface for resolving method parameters into argument ValueUtil in
 * the context of a given request.
 *
 * @author Jay Wu
 * @see HandlerMethodReturnValueHandler
 */
public interface HandlerMethodArgumentResolver {

    /**
     * Whether the given method parameter is supported by this resolver.
     */
    boolean supportsParameter(MethodParameter parameter);

    /**
     * Resolves a method parameter into an argument value from a given context.
     */
    Object resolveArgument(MethodParameter parameter, RoutingContext context) throws Exception;
}