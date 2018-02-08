package org.team4u.fhs.web.handler.method.support;


import cn.hutool.core.convert.Convert;
import org.team4u.fhs.web.RoutingContext;
import org.team4u.fhs.web.handler.method.MethodParameter;
import org.team4u.fhs.web.handler.method.annotation.RequestHeader;

/**
 * @author Jay Wu
 */
public class RequestHeaderArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestHeader.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, RoutingContext context) throws Exception {
        RequestHeader requestHeader = parameter.getParameterAnnotation(RequestHeader.class);

        return Convert.convert(
                parameter.getParameterType(),
                context.getRequest().getHeader(requestHeader.value())
        );
    }
}