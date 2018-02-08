package org.team4u.fhs.web.ext.handler.method.support;

import com.alibaba.fastjson.JSON;
import org.team4u.fhs.web.handler.method.support.RequestBodyArgumentDecoder;

/**
 * @author Jay Wu
 */
public class RequestBodyFastJsonDecoder implements RequestBodyArgumentDecoder {

    @Override
    public Object decode(Class<?> parameterType, byte[] body) {
        return JSON.parseObject(new String(body), parameterType);
    }
}