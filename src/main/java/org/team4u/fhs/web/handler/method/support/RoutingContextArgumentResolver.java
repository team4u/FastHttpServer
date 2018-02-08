package org.team4u.fhs.web.handler.method.support;


import org.team4u.fhs.server.HttpServerRequest;
import org.team4u.fhs.server.HttpServerResponse;
import org.team4u.fhs.server.WebSocketSession;
import org.team4u.fhs.web.RoutingContext;
import org.team4u.fhs.web.handler.method.MethodParameter;

/**
 * @author Jay Wu
 */
public class RoutingContextArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return HttpServerRequest.class.isAssignableFrom(parameter.getParameterType()) ||
                HttpServerResponse.class.isAssignableFrom(parameter.getParameterType()) ||
                WebSocketSession.class.isAssignableFrom(parameter.getParameterType()) ||
                RoutingContext.class.isAssignableFrom(parameter.getNestedParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, RoutingContext context) throws Exception {
        if (HttpServerRequest.class.isAssignableFrom(parameter.getParameterType())) {
            return context.getRequest();
        }

        if (HttpServerResponse.class.isAssignableFrom(parameter.getParameterType())) {
            return context.getResponse();
        }

        if (WebSocketSession.class.isAssignableFrom(parameter.getParameterType())) {
            return context.getWebSocketSession();
        }

        if (RoutingContext.class.isAssignableFrom(parameter.getNestedParameterType())) {
            return context;
        }

        return null;
    }
}