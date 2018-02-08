package org.team4u.fhs.web.handler.method.support;

/**
 * @author Jay Wu
 */
public class DefaultRequestBodyArgumentDecoder implements RequestBodyArgumentDecoder {
    @Override
    public Object decode(Class<?> parameterType, byte[] body) {
        if (parameterType == String.class) {
            return new String(body);
        } else if (parameterType == byte[].class) {
            return body;
        }

        return null;
    }
}