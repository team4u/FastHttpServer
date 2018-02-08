package org.team4u.fhs.server.impl.mock;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import org.team4u.fhs.server.*;
import org.team4u.fhs.server.impl.session.DefaultHttpServerSession;
import org.team4u.kit.core.action.Function;
import org.team4u.kit.core.util.CollectionExUtil;

import java.net.InetSocketAddress;
import java.util.*;

/**
 * @author Jay Wu
 */
public class MockHttpServerRequest implements HttpServerRequest {

    private String path = "";
    private String queryString = "";
    private Map<String, List<String>> params = new HashMap<String, List<String>>();
    private Map<String, String> headers = new HashMap<String, String>();
    private Set<Cookie> cookies = new HashSet<Cookie>();
    private byte[] body;
    private HttpServerSession session = getSession(true);
    private String method = HttpMethod.GET.name();

    @Override
    public String getPath() {
        return path;
    }

    public MockHttpServerRequest setPath(String path) {
        this.path = path;
        return this;
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
    public Object getAttribute(String name) {
        return null;
    }

    @Override
    public Collection<String> getAttributeNames() {
        return null;
    }

    @Override
    public HttpServerRequest setAttribute(String name, Object o) {
        return null;
    }

    @Override
    public HttpServerRequest removeAttribute(String name) {
        return null;
    }

    @Override
    public int getContentLength() {
        return Convert.convert(Integer.class, headers.get(HttpHeaderName.CONTENT_LENGTH.content()), 0);
    }

    @Override
    public String getContentType() {
        return headers.get(HttpHeaderName.CONTENT_TYPE.content());
    }

    @Override
    public String getParameter(String name) {
        List<String> ValueUtil = getParameterMap().get(name);
        if (CollUtil.isEmpty(ValueUtil)) {
            return null;
        }

        return ValueUtil.get(0);
    }

    @Override
    public Set<String> getParameterNames() {
        return params.keySet();
    }

    @Override
    public List<String> getParameterValueUtil(String name) {
        return getParameterMap().get(name);
    }

    @Override
    public byte[] getBody() {
        return body;
    }

    public MockHttpServerRequest setBody(byte[] body) {
        this.body = body;
        return this;
    }

    @Override
    public String getBodyString() {
        return new String(body);
    }

    public MockHttpServerRequest setBodyString(String body) {
        return setBody(body.getBytes());
    }

    @Override
    public Map<String, List<String>> getParameterMap() {
        return params;
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
        return null;
    }

    @Override
    public Collection<Cookie> getCookies() {
        return cookies;
    }

    @Override
    public Cookie getCookie(final String name) {
        return CollectionExUtil.find(cookies, new Function<Cookie, Boolean>() {
            @Override
            public Boolean invoke(Cookie obj) {
                return obj.getName().equals(name);
            }
        });
    }

    @Override
    public String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public Collection<String> getHeaders(String name) {
        return CollectionExUtil.toList(headers.get(name).split(","));
    }

    @Override
    public Set<String> getHeaderNames() {
        return headers.keySet();
    }

    @Override
    public int getIntHeader(String name) {
        return Convert.convert(Integer.class, headers.get(name), 0);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String getMethod() {
        return method;
    }

    public MockHttpServerRequest setMethod(String method) {
        this.method = method;
        return this;
    }

    @Override
    public String getQueryString() {
        return queryString;
    }

    public MockHttpServerRequest setQueryString(String queryString) {
        this.queryString = queryString;
        return this;
    }

    @Override
    public String getRequestURI() {
        return path + "?" + queryString;
    }

    @Override
    public HttpServerSession getSession(boolean create) {
        session = new DefaultHttpServerSession(this, 10);
        return session;
    }

    @Override
    public HttpServerSession getSession() {
        if (session == null) {
            getSession(true);
        }

        return session;
    }
}
