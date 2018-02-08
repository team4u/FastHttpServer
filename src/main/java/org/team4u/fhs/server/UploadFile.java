package org.team4u.fhs.server;

import java.io.File;

/**
 * @author Jay Wu
 */
public interface UploadFile {

    String getName();

    String getFileName();

    String getContentType();

    File getFile();

    void delete();
}