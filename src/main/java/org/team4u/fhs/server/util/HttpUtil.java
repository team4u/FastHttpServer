package org.team4u.fhs.server.util;

import org.team4u.fhs.server.HttpHeaderName;
import org.team4u.fhs.server.HttpServerResponse;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Jay Wu
 */
public class HttpUtil {

    public static final int DEFAULT_CACHE_SECOND = 7 * 60 * 60 * 24;

    public static void setLastModifiedAndCache(HttpServerResponse response, Date lastModified, int cacheSeconds) {
        Date date = new GregorianCalendar().getTime();
        response.setHeader(HttpHeaderName.DATE.content(), GmtTimeUtil.FORMATTER.format(date));
        response.setHeader(HttpHeaderName.EXPIRES.content(), GmtTimeUtil.FORMATTER.format(date));
        response.setHeader(HttpHeaderName.LAST_MODIFIED.content(), GmtTimeUtil.FORMATTER.format(lastModified));
        response.setHeader(HttpHeaderName.CACHE_CONTROL.content(), "private, max-age=" + cacheSeconds);
    }
}