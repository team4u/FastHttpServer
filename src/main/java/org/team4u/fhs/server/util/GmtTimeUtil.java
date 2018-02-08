package org.team4u.fhs.server.util;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Jay Wu
 */
public class GmtTimeUtil {

    public static SimpleDateFormat FORMATTER;

    static {
        FORMATTER = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
        FORMATTER.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
}