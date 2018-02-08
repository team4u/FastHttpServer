package org.team4u.fhs.web.handler.resource;

import java.io.Closeable;

/**
 * @author Jay Wu
 */
public interface StaticResourceLoader extends Closeable {
    /**
     * 根据key获取Resource
     * <p>
     * 仅返回有效资源，若无法找到资源，则返回null
     */
    StaticResource getResource(String id);
}