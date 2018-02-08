package org.team4u.fhs.web.handler.resource;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.util.Date;

/**
 * @author Jay Wu
 */
public class FileResource extends StaticResource {

    private File file = null;
    private long lastModified;

    public FileResource(File file, String path, StaticResourceLoader loader) {
        super(path, loader);

        this.file = file;
        if (file != null) {
            lastModified = file.lastModified();
        } else {
            lastModified = new Date().getTime();
        }
    }

    @Override
    public byte[] getContent() {
        return FileUtil.readBytes(file);
    }

    @Override
    public long lastModified() {
        if (file != null) {
            return file.lastModified();
        } else {
            return lastModified;
        }
    }

    @Override
    public boolean isModified() {
        return lastModified != file.lastModified();
    }

    public File getFile() {
        return file;
    }

    public FileResource setFile(File file) {
        this.file = file;
        lastModified = file.lastModified();
        return this;
    }

    public FileResource setLastModified(long lastModified) {
        this.lastModified = lastModified;
        return this;
    }
}
