package org.team4u.fhs.server.util;

import cn.hutool.core.io.FileUtil;
import org.team4u.kit.core.util.ValueUtil;

import java.io.File;

/**
 * @author Jay Wu
 */
public class MimeUtil {

    //未知情况
    public static final String UNKNOWN_MIME_TYPE = "application/octet-stream";
    //文件夹情况
    public static final String DIRECTORY_MIME_TYPE = "application/directory";

    /**
     * 根据文件获取mime
     */
    public static String getMimeType(File file) {
        if (file.isDirectory()) {
            return DIRECTORY_MIME_TYPE;
        }

        return getMimeType(FileUtil.extName(file));
    }

    /**
     * 根据文件扩展类型获取mime
     */
    public static String getMimeType(String extension) {
        return ValueUtil.defaultIfNull(MimeMappings.DEFAULT.get(extension), UNKNOWN_MIME_TYPE);
    }
}