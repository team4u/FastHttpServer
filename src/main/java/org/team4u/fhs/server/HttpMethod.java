package org.team4u.fhs.server;

/**
 * @author Jay Wu
 */
public enum HttpMethod {
    GET, POST, PUT, PATCH, DELETE, HEAD, TRACE, CONNECT, OPTIONS;

    public static HttpMethod convert(io.netty.handler.codec.http.HttpMethod m) {
        if (m == io.netty.handler.codec.http.HttpMethod.GET) {
            return GET;
        }
        if (m == io.netty.handler.codec.http.HttpMethod.POST) {
            return POST;
        }
        if (m == io.netty.handler.codec.http.HttpMethod.HEAD) {
            return HEAD;
        }
        if (m == io.netty.handler.codec.http.HttpMethod.PUT) {
            return PUT;
        }
        if (m == io.netty.handler.codec.http.HttpMethod.DELETE) {
            return DELETE;
        }
        if (m == io.netty.handler.codec.http.HttpMethod.CONNECT) {
            return CONNECT;
        }
        if (m == io.netty.handler.codec.http.HttpMethod.TRACE) {
            return TRACE;
        }
        if (m == io.netty.handler.codec.http.HttpMethod.OPTIONS) {
            return OPTIONS;
        }
        if (m == io.netty.handler.codec.http.HttpMethod.PATCH) {
            return PATCH;
        }
        return GET;
    }

    public boolean isGet() {
        return this == GET;
    }

    public boolean isPost() {
        return this == POST;
    }

    public boolean isHead() {
        return this == HEAD;
    }
}
