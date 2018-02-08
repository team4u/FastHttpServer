package org.team4u.fhs.web.handler.method.support;

/**
 * @author Jay Wu
 */
public interface RequestBodyArgumentDecoder {
    Object decode(Class<?> parameterType, byte[] body);
}