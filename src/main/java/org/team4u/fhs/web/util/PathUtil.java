package org.team4u.fhs.web.util;


import cn.hutool.core.util.StrUtil;

/**
 * @author Jay Wu
 */
public class PathUtil {

    public static final String PATH_PREFIX = "/";

    public static String normalisePath(String path) {
        if (StrUtil.isEmpty(path)) {
            return PATH_PREFIX;
        }

        if (!path.startsWith(PATH_PREFIX)) {
            path = PATH_PREFIX + path;
        }

        if (path.length() != 1 && path.endsWith(PATH_PREFIX)) {
            path = path.substring(0, path.length() - 1);
        }

        return path;
    }

    public static String endWithPathPrefix(String path) {
        if (path == null) {
            return null;
        }

        if (!path.endsWith(PATH_PREFIX)) {
            path = path + PATH_PREFIX;
        }

        return path;
    }
}