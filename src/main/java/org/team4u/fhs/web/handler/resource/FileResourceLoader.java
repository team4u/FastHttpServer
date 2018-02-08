package org.team4u.fhs.web.handler.resource;

import java.io.File;
import java.io.IOException;

/**
 * @author Jay Wu
 */
public class FileResourceLoader implements StaticResourceLoader {

    @Override
    public StaticResource getResource(String id) {
        final File file = new File(id);

        if (file.isHidden() || !file.exists()) {
            return null;
        }

        if (file.isDirectory() || !file.isFile()) {
            return null;
        }

        return new FileResource(file, id, this);
    }

    @Override
    public void close() throws IOException {
    }
}