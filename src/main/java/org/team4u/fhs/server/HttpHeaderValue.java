package org.team4u.fhs.server;

/**
 * @author Jay Wu
 */
public enum HttpHeaderValue {

    /**
     * {@code "application/json"}
     */
    APPLICATION_JSON("application/json"),
    /**
     * {@code "application/x-www-form-urlencoded"}
     */
    APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),
    /**
     * {@code "application/octet-stream"}
     */
    APPLICATION_OCTET_STREAM("application/octet-stream"),
    /**
     * {@code "attachment"}
     */
    ATTACHMENT("attachment"),
    /**
     * {@code "base64"}
     */
    BASE64("base64"),
    /**
     * {@code "binary"}
     */
    BINARY("binary"),
    /**
     * {@code "boundary"}
     */
    BOUNDARY("boundary"),
    /**
     * {@code "bytes"}
     */
    BYTES("bytes"),
    /**
     * {@code "charset"}
     */
    CHARSET("charset"),
    /**
     * {@code "chunked"}
     */
    CHUNKED("chunked"),
    /**
     * {@code "close"}
     */
    CLOSE("close"),
    /**
     * {@code "compress"}
     */
    COMPRESS("compress"),
    /**
     * {@code "100-continue"}
     */
    CONTINUE("100-continue"),
    /**
     * {@code "deflate"}
     */
    DEFLATE("deflate"),
    /**
     * {@code "x-deflate"}
     */
    X_DEFLATE("x-deflate"),
    /**
     * {@code "file"}
     */
    FILE("file"),
    /**
     * {@code "filename"}
     * See {@link HttpHeaderName#CONTENT_DISPOSITION}
     */
    FILENAME("filename"),
    /**
     * {@code "form-data"}
     * See {@link HttpHeaderName#CONTENT_DISPOSITION}
     */
    FORM_DATA("form-data"),
    /**
     * {@code "gzip"}
     */
    GZIP("gzip"),
    /**
     * {@code "gzip,deflate"}
     */
    GZIP_DEFLATE("gzip,deflate"),
    /**
     * {@code "x-gzip"}
     */
    X_GZIP("x-gzip"),
    /**
     * {@code "identity"}
     */
    IDENTITY("identity"),
    /**
     * {@code "keep-alive"}
     */
    KEEP_ALIVE("keep-alive"),
    /**
     * {@code "max-age"}
     */
    MAX_AGE("max-age"),
    /**
     * {@code "max-stale"}
     */
    MAX_STALE("max-stale"),
    /**
     * {@code "min-fresh"}
     */
    MIN_FRESH("min-fresh"),
    /**
     * {@code "multipart/form-data"}
     */
    MULTIPART_FORM_DATA("multipart/form-data"),
    /**
     * {@code "multipart/mixed"}
     */
    MULTIPART_MIXED("multipart/mixed"),
    /**
     * {@code "must-revalidate"}
     */
    MUST_REVALIDATE("must-revalidate"),
    /**
     * {@code "name"}
     * See {@link HttpHeaderName#CONTENT_DISPOSITION}
     */
    NAME("name"),
    /**
     * {@code "no-cache"}
     */
    NO_CACHE("no-cache"),
    /**
     * {@code "no-store"}
     */
    NO_STORE("no-store"),
    /**
     * {@code "no-transform"}
     */
    NO_TRANSFORM("no-transform"),
    /**
     * {@code "none"}
     */
    NONE("none"),
    /**
     * {@code "0"}
     */
    ZERO("0"),
    /**
     * {@code "only-if-cached"}
     */
    ONLY_IF_CACHED("only-if-cached"),
    /**
     * {@code "private"}
     */
    PRIVATE("private"),
    /**
     * {@code "proxy-revalidate"}
     */
    PROXY_REVALIDATE("proxy-revalidate"),
    /**
     * {@code "public"}
     */
    PUBLIC("public"),
    /**
     * {@code "quoted-printable"}
     */
    QUOTED_PRINTABLE("quoted-printable"),
    /**
     * {@code "s-maxage"}
     */
    S_MAXAGE("s-maxage"),
    /**
     * {@code "text/plain"}
     */
    TEXT_PLAIN("text/plain"),

    TEXT_HTML("text/html"),

    TEXT_XML("text/xml"),
    /**
     * {@code "trailers"}
     */
    TRAILERS("trailers"),
    /**
     * {@code "upgrade"}
     */
    UPGRADE("upgrade"),
    /**
     * {@code "websocket"}
     */
    WEBSOCKET("websocket");

    private String content;

    HttpHeaderValue(String content) {
        this.content = content;
    }

    public String content() {
        return content;
    }
}