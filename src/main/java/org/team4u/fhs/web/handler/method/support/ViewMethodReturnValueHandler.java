package org.team4u.fhs.web.handler.method.support;

import cn.hutool.core.util.StrUtil;
import org.team4u.fhs.server.HttpServerRequest;
import org.team4u.fhs.web.ModelAndView;
import org.team4u.fhs.web.handler.method.MethodParameter;
import org.team4u.fhs.web.handler.method.annotation.ResponseView;
import org.team4u.kit.core.util.ValueUtil;

import java.util.Map;

/**
 * @author Jay Wu
 */
public class ViewMethodReturnValueHandler implements HandlerMethodReturnValueHandler {

    @Override
    public boolean supportsReturnType(MethodParameter parameter) {
        return parameter.hasMethodAnnotation(ResponseView.class) ||
                parameter.getDeclaringClass().isAnnotationPresent(ResponseView.class);
    }

    @Override
    public ModelAndView handleReturnValue(Object returnValue, MethodParameter parameter, HttpServerRequest httpRequest)
            throws Exception {
        ResponseView methodResponseView = parameter.getMethodAnnotation(ResponseView.class);
        ResponseView classResponseView = parameter.getDeclaringClass().getAnnotation(ResponseView.class);

        String successViewName = null;
        String errorViewName = null;
        if (methodResponseView != null) {
            successViewName = methodResponseView.success();
            errorViewName = methodResponseView.error();
        }

        if (classResponseView != null) {
            successViewName = ValueUtil.defaultIfNull(successViewName, classResponseView.success());
            errorViewName = ValueUtil.defaultIfNull(errorViewName, classResponseView.error());
        }

        if (returnValue instanceof Exception) {
            if (StrUtil.isEmpty(errorViewName)) {
                throw (Exception) returnValue;
            }

            return new ModelAndView(errorViewName).addObject(returnValue);
        } else {
            if (StrUtil.isEmpty(successViewName)) {
                return null;
            }

            if (returnValue instanceof Map) {
                //noinspection unchecked
                return new ModelAndView(successViewName, (Map<String, ?>) returnValue);
            } else {
                return new ModelAndView(successViewName).addObject(returnValue);
            }
        }
    }
}