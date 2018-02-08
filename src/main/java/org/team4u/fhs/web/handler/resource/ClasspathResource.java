package org.team4u.fhs.web.handler.resource;

import cn.hutool.core.io.IoUtil;
import org.team4u.kit.core.error.ExceptionUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author Jay Wu
 */
public class ClasspathResource extends FileResource {

    private URL url;

    public ClasspathResource(URL url, String path, StaticResourceLoader loader) {
        super(null, path, loader);

        this.url = url;

        if (url.getProtocol().equals("file")) {
            File file = new File(url.getFile());
            setFile(file);
            setLastModified(file.lastModified());
        }
    }

    @Override
    public byte[] getContent() {
        try {
            return IoUtil.readBytes(url.openStream());
        } catch (IOException e) {
            throw ExceptionUtil.toRuntimeException(e);
        }
    }

    @Override
    public boolean isModified() {
        return getFile() != null && isModified();
    }
}