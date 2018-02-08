package org.team4u.fhs.web.util;

import cn.hutool.core.util.StrUtil;
import org.team4u.fhs.server.*;
import org.team4u.kit.core.util.ValueUtil;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Jay Wu
 */
public class WebSocketAdapterRequest implements HttpServerRequest {

    private byte[] binaryBody;
    private String textBody;
    private String virtualPath;

    private WebSocketSession webSocketSession;

    public WebSocketAdapterRequest(String virtualPath, WebSocketSession webSocketSession) {
        this.virtualPath = webSocketSession.getPath() + PathUtil.normalisePath(virtualPath);
        this.webSocketSession = webSocketSession;
    }

    public WebSocketAdapterRequest(String virtualPath, String textBody, WebSocketSession webSocketSession) {
        this(virtualPath, webSocketSession);
        this.textBody = textBody;
    }

    public WebSocketAdapterRequest(String virtualPath, byte[] binaryBody, WebSocketSession webSocketSession) {
        this(virtualPath, webSocketSession);
        this.binaryBody = binaryBody;
    }

    @Override
    public Object getAttribute(String name) {
        return webSocketSession.getAttribute(name);
    }

    @Override
    public Collection<String> getAttributeNames() {
        return webSocketSession.getAttributeNames();
    }

    @Override
    public HttpServerRequest setAttribute(String name, Object o) {
        webSocketSession.setAttribute(name, o);
        return this;
    }

    @Override
    public HttpServerRequest removeAttribute(String name) {
        webSocketSession.removeAttribute(name);
        return this;
    }

    @Override
    public int getContentLength() {
        return getIntHeader(HttpHeaderName.CONTENT_LENGTH.content());
    }

    @Override
    public String getContentType() {
        return webSocketSession.getHeader(HttpHeaderName.CONTENT_TYPE.content());
    }

    @Override
    public String getParameter(String name) {
        return webSocketSession.getParameter(name);
    }

    @Override
    public Set<String> getParameterNames() {
        return webSocketSession.getParameterNames();
    }

    @Override
    public List<String> getParameterValueUtil(String name) {
        return webSocketSession.getParameterValueUtil(name);
    }

    @Override
    public Map<String, List<String>> getParameterMap() {
        return webSocketSession.getParameterMap();
    }

    @Override
    public byte[] getBody() {
        return ValueUtil.defaultIfNull(binaryBody, textBody == null ? null : textBody.getBytes());
    }

    @Override
    public String getBodyString() {
        return textBody;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return webSocketSession.getRemoteAddress();
    }

    @Override
    public Collection<Cookie> getCookies() {
        return null;
    }

    @Override
    public Cookie getCookie(String name) {
        return null;
    }

    @Override
    public String getHeader(String name) {
        return webSocketSession.getHeader(name);
    }

    @Override
    public Collection<String> getHeaders(String name) {
        return webSocketSession.getHeaders(name);
    }

    @Override
    public Set<String> getHeaderNames() {
        return webSocketSession.getHeaderNames();
    }

    @Override
    public int getIntHeader(String name) {
        return webSocketSession.getIntHeader(name);
    }

    @Override
    public String getMethod() {
        return null;
    }

    @Override
    public String getQueryString() {
        return webSocketSession.getQueryString();
    }

    @Override
    public String getRequestURI() {
        String queryString = getQueryString();
        if (StrUtil.isEmpty(queryString)) {
            queryString = "?" + queryString;
        } else {
            queryString = "";
        }

        return getPath() + queryString;
    }

    @Override
    public String getPath() {
        return virtualPath;
    }

    @Override
    public List<UploadFile> getUploadFiles() {
        return null;
    }

    @Override
    public UploadFile getUploadFile(String name) {
        return null;
    }

    @Override
    public List<UploadFile> getUploadFiles(String name) {
        return null;
    }

    @Override
    public HttpServerSession getSession(boolean create) {
        return null;
    }

    @Override
    public HttpServerSession getSession() {
        return null;
    }
}