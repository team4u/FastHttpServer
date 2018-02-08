package org.team4u.fhs.server;

/**
 * @author Jay Wu
 */
public enum HttpHeaderName {

    /**
     * {@code "accept"}
     */
    ACCEPT("accept"),
    /**
     * {@code "accept-charset"}
     */
    ACCEPT_CHARSET("accept-charset"),
    /**
     * {@code "accept-encoding"}
     */
    ACCEPT_ENCODING("accept-encoding"),
    /**
     * {@code "accept-language"}
     */
    ACCEPT_LANGUAGE("accept-language"),
    /**
     * {@code "accept-ranges"}
     */
    ACCEPT_RANGES("accept-ranges"),
    /**
     * {@code "accept-patch"}
     */
    ACCEPT_PATCH("accept-patch"),
    /**
     * {@code "access-control-allow-credentials"}
     */
    ACCESS_CONTROL_ALLOW_CREDENTIALS("access-control-allow-credentials"),
    /**
     * {@code "access-control-allow-headers"}
     */
    ACCESS_CONTROL_ALLOW_HEADERS("access-control-allow-headers"),
    /**
     * {@code "access-control-allow-methods"}
     */
    ACCESS_CONTROL_ALLOW_METHODS("access-control-allow-methods"),
    /**
     * {@code "access-control-allow-origin"}
     */
    ACCESS_CONTROL_ALLOW_ORIGIN("access-control-allow-origin"),
    /**
     * {@code "access-control-expose-headers"}
     */
    ACCESS_CONTROL_EXPOSE_HEADERS("access-control-expose-headers"),
    /**
     * {@code "access-control-max-age"}
     */
    ACCESS_CONTROL_MAX_AGE("access-control-max-age"),
    /**
     * {@code "access-control-request-headers"}
     */
    ACCESS_CONTROL_REQUEST_HEADERS("access-control-request-headers"),
    /**
     * {@code "access-control-request-method"}
     */
    ACCESS_CONTROL_REQUEST_METHOD("access-control-request-method"),
    /**
     * {@code "age"}
     */
    AGE("age"),
    /**
     * {@code "allow"}
     */
    ALLOW("allow"),
    /**
     * {@code "authorization"}
     */
    AUTHORIZATION("authorization"),
    /**
     * {@code "cache-control"}
     */
    CACHE_CONTROL("cache-control"),
    /**
     * {@code "connection"}
     */
    CONNECTION("connection"),
    /**
     * {@code "content-base"}
     */
    CONTENT_BASE("content-base"),
    /**
     * {@code "content-encoding"}
     */
    CONTENT_ENCODING("content-encoding"),
    /**
     * {@code "content-language"}
     */
    CONTENT_LANGUAGE("content-language"),
    /**
     * {@code "content-length"}
     */
    CONTENT_LENGTH("content-length"),
    /**
     * {@code "content-location"}
     */
    CONTENT_LOCATION("content-location"),
    /**
     * {@code "content-transfer-encoding"}
     */
    CONTENT_TRANSFER_ENCODING("content-transfer-encoding"),
    /**
     * {@code "content-disposition"}
     */
    CONTENT_DISPOSITION("content-disposition"),
    /**
     * {@code "content-md5"}
     */
    CONTENT_MD5("content-md5"),
    /**
     * {@code "content-range"}
     */
    CONTENT_RANGE("content-range"),
    /**
     * {@code "content-type"}
     */
    CONTENT_TYPE("content-type"),
    /**
     * {@code "cookie"}
     */
    COOKIE("cookie"),
    /**
     * {@code "date"}
     */
    DATE("date"),
    /**
     * {@code "etag"}
     */
    ETAG("etag"),
    /**
     * {@code "expect"}
     */
    EXPECT("expect"),
    /**
     * {@code "expires"}
     */
    EXPIRES("expires"),
    /**
     * {@code "from"}
     */
    FROM("from"),
    /**
     * {@code "host"}
     */
    HOST("host"),
    /**
     * {@code "if-match"}
     */
    IF_MATCH("if-match"),
    /**
     * {@code "if-modified-since"}
     */
    IF_MODIFIED_SINCE("if-modified-since"),
    /**
     * {@code "if-none-match"}
     */
    IF_NONE_MATCH("if-none-match"),
    /**
     * {@code "if-range"}
     */
    IF_RANGE("if-range"),
    /**
     * {@code "if-unmodified-since"}
     */
    IF_UNMODIFIED_SINCE("if-unmodified-since"),
    /**
     * @deprecated use {@link #CONNECTION}
     * <p>
     * {@code "keep-alive"}
     */
    @Deprecated
    KEEP_ALIVE("keep-alive"),
    /**
     * {@code "last-modified"}
     */
    LAST_MODIFIED("last-modified"),
    /**
     * {@code "location"}
     */
    LOCATION("location"),
    /**
     * {@code "max-forwards"}
     */
    MAX_FORWARDS("max-forwards"),
    /**
     * {@code "origin"}
     */
    ORIGIN("origin"),
    /**
     * {@code "pragma"}
     */
    PRAGMA("pragma"),
    /**
     * {@code "proxy-authenticate"}
     */
    PROXY_AUTHENTICATE("proxy-authenticate"),
    /**
     * {@code "proxy-authorization"}
     */
    PROXY_AUTHORIZATION("proxy-authorization"),
    /**
     * @deprecated use {@link #CONNECTION}
     * <p>
     * {@code "proxy-connection"}
     */
    @Deprecated
    PROXY_CONNECTION("proxy-connection"),
    /**
     * {@code "range"}
     */
    RANGE("range"),
    /**
     * {@code "referer"}
     */
    REFERER("referer"),
    /**
     * {@code "retry-after"}
     */
    RETRY_AFTER("retry-after"),
    /**
     * {@code "sec-websocket-key1"}
     */
    SEC_WEBSOCKET_KEY1("sec-websocket-key1"),
    /**
     * {@code "sec-websocket-key2"}
     */
    SEC_WEBSOCKET_KEY2("sec-websocket-key2"),
    /**
     * {@code "sec-websocket-location"}
     */
    SEC_WEBSOCKET_LOCATION("sec-websocket-location"),
    /**
     * {@code "sec-websocket-origin"}
     */
    SEC_WEBSOCKET_ORIGIN("sec-websocket-origin"),
    /**
     * {@code "sec-websocket-protocol"}
     */
    SEC_WEBSOCKET_PROTOCOL("sec-websocket-protocol"),
    /**
     * {@code "sec-websocket-version"}
     */
    SEC_WEBSOCKET_VERSION("sec-websocket-version"),
    /**
     * {@code "sec-websocket-key"}
     */
    SEC_WEBSOCKET_KEY("sec-websocket-key"),
    /**
     * {@code "sec-websocket-accept"}
     */
    SEC_WEBSOCKET_ACCEPT("sec-websocket-accept"),
    /**
     * {@code "sec-websocket-protocol"}
     */
    SEC_WEBSOCKET_EXTENSIONS("sec-websocket-extensions"),
    /**
     * {@code "server"}
     */
    SERVER("server"),
    /**
     * {@code "set-cookie"}
     */
    SET_COOKIE("set-cookie"),
    /**
     * {@code "set-cookie2"}
     */
    SET_COOKIE2("set-cookie2"),
    /**
     * {@code "te"}
     */
    TE("te"),
    /**
     * {@code "trailer"}
     */
    TRAILER("trailer"),
    /**
     * {@code "transfer-encoding"}
     */
    TRANSFER_ENCODING("transfer-encoding"),
    /**
     * {@code "upgrade"}
     */
    UPGRADE("upgrade"),
    /**
     * {@code "user-agent"}
     */
    USER_AGENT("user-agent"),
    /**
     * {@code "vary"}
     */
    VARY("vary"),
    /**
     * {@code "via"}
     */
    VIA("via"),
    /**
     * {@code "warning"}
     */
    WARNING("warning"),
    /**
     * {@code "websocket-location"}
     */
    WEBSOCKET_LOCATION("websocket-location"),
    /**
     * {@code "websocket-origin"}
     */
    WEBSOCKET_ORIGIN("websocket-origin"),
    /**
     * {@code "websocket-protocol"}
     */
    WEBSOCKET_PROTOCOL("websocket-protocol"),
    /**
     * {@code "www-authenticate"}
     */
    WWW_AUTHENTICATE("www-authenticate");


    private String content;

    HttpHeaderName(String content) {
        this.content = content;
    }

    public String content() {
        return content;
    }
}