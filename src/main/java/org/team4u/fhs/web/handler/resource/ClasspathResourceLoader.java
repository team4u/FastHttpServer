package org.team4u.fhs.web.handler.resource;

import java.io.IOException;
import java.net.URL;

/**
 * @author Jay Wu
 */
public class ClasspathResourceLoader implements StaticResourceLoader {

    private ClassLoader classLoader;

    public ClasspathResourceLoader() {
        this(ClasspathResourceLoader.class.getClassLoader());
    }

    public ClasspathResourceLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public StaticResource getResource(String id) {
        URL url = classLoader.getResource(id);
        if (url == null) {
            return null;
        }

        return new ClasspathResource(url, id, this);
    }

    @Override
    public void close() throws IOException {
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }
}