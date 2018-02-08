package org.team4u.fhs.server.impl.netty;

import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.MemoryAttribute;

import java.io.IOException;

/**
 * 内存参数
 *
 * @author Jay Wu
 */
class MemoryAttributeHttpDataFactory extends DefaultHttpDataFactory {

    public MemoryAttributeHttpDataFactory(boolean useDisk) {
        super(useDisk);
    }

    @Override
    public Attribute createAttribute(HttpRequest request, String name) {
        MemoryAttribute attribute = new MemoryAttribute(name);
        attribute.setMaxSize(-1);
        return attribute;
    }

    @Override
    public Attribute createAttribute(HttpRequest request, String name, String value) {
        try {
            MemoryAttribute attribute = new MemoryAttribute(name, value, HttpConstants.DEFAULT_CHARSET);
            attribute.setMaxSize(-1);
            return attribute;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}