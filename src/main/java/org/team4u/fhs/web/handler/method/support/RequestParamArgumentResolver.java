package org.team4u.fhs.web.handler.method.support;


import cn.hutool.core.convert.Convert;
import org.team4u.fhs.server.UploadFile;
import org.team4u.fhs.web.RoutingContext;
import org.team4u.fhs.web.handler.method.MethodParameter;
import org.team4u.fhs.web.handler.method.annotation.RequestParam;
import org.team4u.kit.core.codec.CodecRegistry;
import org.team4u.kit.core.util.MapExUtil;

import java.util.Collection;
import java.util.Map;

/**
 * @author Jay Wu
 */
public class RequestParamArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, RoutingContext context) throws Exception {
        RequestParam requestParam = parameter.getParameterAnnotation(RequestParam.class);

        if (UploadFile.class.isAssignableFrom(parameter.getParameterType())) {
            return context.getRequest().getUploadFile(requestParam.value());
        }

        if (requestParam.value().equals("..")) {
            Map<String, String> params = CodecRegistry.URL_TO_MAP_CODEC.encode(
                    CodecRegistry.URL_TO_MAP_LIST_CODEC.decode(context.getRequest().getParameterMap())
            );
            Map<String, ?> pathMap = MapExUtil.toPathMap(params);
            return MapExUtil.toObject(pathMap, parameter.getParameterType());
        }

        Object value;
        if (Collection.class.isAssignableFrom(parameter.getParameterType()) ||
                parameter.getParameterType().isArray()) {
            value = context.getRequest().getParameterValueUtil(requestParam.value());
        } else {
            value = context.getRequest().getParameter(requestParam.value());
        }

        return Convert.convert(parameter.getParameterType(), value);
    }
}