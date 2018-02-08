package org.team4u.fhs.web.handler.method.support;


import cn.hutool.core.convert.Convert;
import org.team4u.fhs.server.Cookie;
import org.team4u.fhs.web.RoutingContext;
import org.team4u.fhs.web.handler.method.MethodParameter;
import org.team4u.fhs.web.handler.method.annotation.CookieValue;

/**
 * @author Jay Wu
 */
public class CookieValueArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CookieValue.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, RoutingContext context) throws Exception {
        CookieValue cookieValue = parameter.getParameterAnnotation(CookieValue.class);
        Cookie cookie = context.getRequest().getCookie(cookieValue.value());
        if (cookie == null) {
            return null;
        }

        return Convert.convert(parameter.getParameterType(), cookie.getValue()
        );
    }
}