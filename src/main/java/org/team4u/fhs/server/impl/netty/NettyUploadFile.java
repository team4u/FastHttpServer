package org.team4u.fhs.server.impl.netty;

import cn.hutool.core.io.FileUtil;
import io.netty.handler.codec.http.multipart.DiskFileUpload;
import io.netty.handler.codec.http.multipart.FileUpload;
import org.team4u.fhs.server.UploadFile;

import java.io.File;
import java.io.IOException;

/**
 * @author Jay Wu
 */
public class NettyUploadFile implements UploadFile {

    private FileUpload delegate;
    private File file;

    public NettyUploadFile(FileUpload delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public String getFileName() {
        return delegate.getFilename();
    }

    @Override
    public String getContentType() {
        return delegate.getContentType();
    }

    @Override
    public File getFile() {
        if (file == null) {
            try {
                if (delegate instanceof DiskFileUpload) {
                    DiskFileUpload diskFileUpload = (DiskFileUpload) delegate;
                    file = diskFileUpload.getFile();
                } else {
                    file = createTempFile(true, DiskFileUpload.prefix,
                            DiskFileUpload.baseDirectory, delegate.getFilename());
                    FileUtil.writeBytes(delegate.get(), file);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return file;
    }

    @Override
    public void delete() {
        FileUtil.del(file);
        delegate.delete();
    }

    private File createTempFile(boolean deleteOnExit, String preFix, String tmpDir, String suffix) throws IOException {
        File tempFile;
        if (tmpDir == null) {
            tempFile = File.createTempFile(preFix, suffix);
        } else {
            tempFile = File.createTempFile(preFix, suffix, new File(tmpDir));
        }
        if (deleteOnExit) {
            tempFile.deleteOnExit();
        }
        return tempFile;
    }
}