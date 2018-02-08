package org.team4u.fhs.server.impl.mock;

import cn.hutool.core.lang.Dict;
import org.team4u.fhs.server.Cookie;
import org.team4u.fhs.server.HttpHeaderName;
import org.team4u.fhs.server.HttpServerResponse;
import org.team4u.fhs.server.HttpStatusCode;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Jay Wu
 */
public class MockHttpServerResponse implements HttpServerResponse {

    private int status = HttpStatusCode.OK.code();
    private Set<Cookie> cookies = new HashSet<Cookie>();
    private Dict headers = new Dict();
    private byte[] body;
    private boolean committed = false;
    private File file;

    @Override
    public void setContentLength(long len) {
        headers.put(HttpHeaderName.CONTENT_LENGTH.content(), len);
    }

    @Override
    public String getContentType() {
        return headers.getStr(HttpHeaderName.CONTENT_TYPE.content());
    }

    @Override
    public void setContentType(String type) {
        headers.put(HttpHeaderName.CONTENT_TYPE.content(), type);
    }

    @Override
    public HttpServerResponse addCookie(Cookie cookie) {
        cookies.add(cookie);
        return this;
    }

    public Set<Cookie> getCookies() {
        return cookies;
    }

    @Override
    public boolean containsHeader(String name) {
        return headers.containsKey(name);
    }

    @Override
    public HttpServerResponse setHeader(String name, Object value) {
        headers.put(name, value);
        return this;
    }

    @Override
    public HttpServerResponse addHeader(String name, Object value) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpServerResponse setStatus(int status) {
        this.status = status;
        return this;
    }

    @Override
    public HttpServerResponse sendError(int status) {
        setStatus(status);
        return this;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getHeader(String name) {
        return headers.getStr(name);
    }

    @Override
    public Collection<String> getHeaders(String name) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> getHeaderNames() {
        return headers.keySet();
    }

    @Override
    public HttpServerResponse write(String data) {
        body = data.getBytes();
        return this;
    }

    @Override
    public HttpServerResponse write(byte[] data) {
        body = data;
        return this;
    }

    @Override
    public HttpServerResponse write(File file) {
        this.file = file;
        return this;
    }

    @Override
    public HttpServerResponse flush() {
        committed = true;
        return this;
    }

    @Override
    public boolean isCommitted() {
        return committed;
    }

    public byte[] getBody() {
        return body;
    }

    public File getFile() {
        return file;
    }

    public Dict getHeaders() {
        return headers;
    }
}