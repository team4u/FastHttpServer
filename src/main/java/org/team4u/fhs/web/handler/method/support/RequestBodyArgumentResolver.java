package org.team4u.fhs.web.handler.method.support;


import org.team4u.fhs.web.RoutingContext;
import org.team4u.fhs.web.handler.method.MethodParameter;
import org.team4u.fhs.web.handler.method.annotation.RequestBody;
import org.team4u.kit.core.lang.ServiceProvider;

/**
 * @author Jay Wu
 */
public class RequestBodyArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, RoutingContext context) throws Exception {
        RequestBody requestBody = parameter.getParameterAnnotation(RequestBody.class);
        RequestBodyArgumentDecoder decoder = ServiceProvider.getInstance().getOrRegister(requestBody.value());
        return decoder.decode(parameter.getParameterType(), context.getRequest().getBody());
    }
}