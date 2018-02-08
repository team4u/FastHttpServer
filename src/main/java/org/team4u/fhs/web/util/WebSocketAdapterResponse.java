package org.team4u.fhs.web.util;

import org.team4u.fhs.server.Cookie;
import org.team4u.fhs.server.HttpServerResponse;
import org.team4u.fhs.server.WebSocketSession;
import org.team4u.kit.core.error.ServiceException;

import java.io.File;
import java.util.Collection;
import java.util.Set;

/**
 * @author Jay Wu
 */
public class WebSocketAdapterResponse implements HttpServerResponse {

    private WebSocketSession webSocketSession;

    public WebSocketAdapterResponse(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
    }

    @Override
    public void setContentLength(long len) {

    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public void setContentType(String type) {
    }

    @Override
    public HttpServerResponse addCookie(Cookie cookie) {
        return this;
    }

    @Override
    public boolean containsHeader(String name) {
        return false;
    }

    @Override
    public HttpServerResponse setHeader(String name, Object value) {
        return this;
    }

    @Override
    public HttpServerResponse addHeader(String name, Object value) {
        return this;
    }

    @Override
    public HttpServerResponse setStatus(int status) {
        return this;
    }

    @Override
    public HttpServerResponse sendError(int status) {
        throw new ServiceException(status + "", "");
    }

    @Override
    public int getStatus() {
        return 0;
    }

    @Override
    public String getHeader(String name) {
        return null;
    }

    @Override
    public Collection<String> getHeaders(String name) {
        return null;
    }

    @Override
    public Set<String> getHeaderNames() {
        return null;
    }

    @Override
    public HttpServerResponse write(String data) {
        webSocketSession.write(data);
        return this;
    }

    @Override
    public HttpServerResponse write(byte[] data) {
        webSocketSession.write(data);
        return this;
    }

    @Override
    public HttpServerResponse write(File file) {
        return this;
    }

    @Override
    public HttpServerResponse flush() {
        return this;
    }

    @Override
    public boolean isCommitted() {
        return !webSocketSession.isOpen();
    }
}