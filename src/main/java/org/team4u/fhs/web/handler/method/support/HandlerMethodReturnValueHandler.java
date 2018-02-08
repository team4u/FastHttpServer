package org.team4u.fhs.web.handler.method.support;

import org.team4u.fhs.server.HttpServerRequest;
import org.team4u.fhs.web.ModelAndView;
import org.team4u.fhs.web.handler.method.MethodParameter;

/**
 * Strategy interface to handle the value returned from the invocation of a
 * handler method.
 *
 * @author Jay Wu
 * @see HandlerMethodArgumentResolver
 */
public interface HandlerMethodReturnValueHandler {

    /**
     * Whether the given method return type is
     * supported by this handler.
     *
     * @return {@code true} if this handler supports the supplied return type;
     * {@code false} otherwise
     */
    boolean supportsReturnType(MethodParameter parameter);

    /**
     * Handle the given return value by adding attributes to the model and
     * setting a view
     */
    ModelAndView handleReturnValue(Object returnValue, MethodParameter parameter, HttpServerRequest httpRequest) throws Exception;

}
