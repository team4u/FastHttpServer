package org.team4u.fhs.server.impl.netty;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import org.team4u.fhs.server.*;
import org.team4u.fhs.server.impl.session.SessionManager;
import org.team4u.kit.core.action.Function;
import org.team4u.kit.core.error.ExceptionUtil;
import org.team4u.kit.core.util.CollectionExUtil;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static io.netty.util.internal.EmptyArrays.EMPTY_BYTES;

public class NettyHttpServerRequest implements HttpServerRequest {

    protected static final Map<String, List<String>> EMPTY_POST_PARAMS = new HashMap<String, List<String>>();
    protected SessionManager sessionManager;
    protected Map<String, List<String>> params;
    protected Dict attributes;
    protected byte[] bodyBytes;
    protected Set<Cookie> cookies;
    protected List<UploadFile> files;
    protected URI uri;
    protected InetSocketAddress remoteAddress;
    protected InetSocketAddress localAddress;
    protected FullHttpRequest nettyRequest;
    protected HttpPostRequestDecoder httpPostRequestDecoder;
    protected ChannelHandlerContext nettyChannelHandlerContext;
    protected HttpServerSession session;

    public NettyHttpServerRequest(SessionManager sessionManager,
                                  ChannelHandlerContext nettyChannelHandlerContext,
                                  FullHttpRequest nettyRequest) {
        this.sessionManager = sessionManager;
        this.nettyRequest = nettyRequest;
        this.nettyChannelHandlerContext = nettyChannelHandlerContext;

        try {
            uri = new URI(nettyRequest.uri());
        } catch (URISyntaxException e) {
            // Ignore error
        }

        remoteAddress = (InetSocketAddress) nettyChannelHandlerContext.channel().remoteAddress();
        localAddress = (InetSocketAddress) nettyChannelHandlerContext.channel().localAddress();
    }

    @Override
    public Object getAttribute(String name) {
        return getAttributes().get(name);
    }

    @Override
    public Collection<String> getAttributeNames() {
        return getAttributes().keySet();
    }

    @Override
    public NettyHttpServerRequest setAttribute(String name, Object o) {
        getAttributes().put(name, o);
        return this;
    }

    @Override
    public NettyHttpServerRequest removeAttribute(String name) {
        getAttributes().remove(name);
        return this;
    }

    @Override
    public int getContentLength() {
        return getIntHeader(HttpHeaderName.CONTENT_LENGTH.content());
    }

    @Override
    public String getContentType() {
        return getHeader(HttpHeaderName.CONTENT_TYPE.content());
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
        return getParameterMap().keySet();
    }

    @Override
    public List<String> getParameterValueUtil(String name) {
        return getParameterMap().get(name);
    }

    @Override
    public Map<String, List<String>> getParameterMap() {
        if (params == null) {
            params = new HashMap<String, List<String>>();
            params.putAll(handleUrlParams());
            params.putAll(handleHttpPostParams());
        }

        return params;
    }

    @Override
    public byte[] getBody() {
        if (bodyBytes == null) {
            if (nettyRequest.content().readableBytes() == 0) {
                bodyBytes = EMPTY_BYTES;
            } else {
                bodyBytes = new byte[nettyRequest.content().readableBytes()];
                nettyRequest.content().getBytes(nettyRequest.content().readerIndex(), bodyBytes);
            }
        }

        return bodyBytes;
    }

    @Override
    public String getBodyString() {
        return new String(getBody());
    }

    @Override
    public String getProtocol() {
        return nettyRequest.protocolVersion().protocolName();
    }

    @Override
    public String getScheme() {
        return uri.getScheme();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    @Override
    public Collection<Cookie> getCookies() {
        if (cookies == null) {
            String cookieValue = nettyRequest.headers().get(HttpHeaderName.COOKIE.content());
            if (StrUtil.isEmpty(cookieValue)) {
                //noinspection unchecked
                cookies = Collections.EMPTY_SET;
                return cookies;
            }

            Set<io.netty.handler.codec.http.cookie.Cookie> nettyCookies = ServerCookieDecoder.LAX.decode(cookieValue);

            if (CollUtil.isEmpty(nettyCookies)) {
                //noinspection unchecked
                cookies = Collections.EMPTY_SET;
                return cookies;
            }

            cookies = new HashSet<Cookie>();
            for (io.netty.handler.codec.http.cookie.Cookie nettyCookie : nettyCookies) {
                Cookie cookie = new Cookie(nettyCookie.name(), nettyCookie.value());
                cookie.setDomain(nettyCookie.domain());
                cookie.setMaxAge((int) nettyCookie.maxAge());
                cookie.setPath(nettyCookie.path());
                cookie.setHttpOnly(nettyCookie.isHttpOnly());
                cookie.setVersion(0);

                cookies.add(cookie);
            }
        }

        return cookies;
    }

    public Cookie getCookie(String name) {
        for (Cookie cookie : getCookies()) {
            if (StrUtil.equals(cookie.getName(), name)) {
                return cookie;
            }
        }

        return null;
    }

    @Override
    public String getHeader(String name) {
        return nettyRequest.headers().get(name);
    }

    @Override
    public Collection<String> getHeaders(String name) {
        return nettyRequest.headers().getAll(name);
    }

    @Override
    public Set<String> getHeaderNames() {
        return nettyRequest.headers().names();
    }

    @Override
    public int getIntHeader(String name) {
        return Integer.valueOf(getHeader(name));
    }

    @Override
    public String getMethod() {
        return nettyRequest.method().name();
    }

    @Override
    public String getQueryString() {
        return uri.getQuery();
    }

    @Override
    public String getRequestURI() {
        return nettyRequest.uri();
    }

    @Override
    public String getPath() {
        return uri.getPath();
    }

    @Override
    public List<UploadFile> getUploadFiles() {
        if (files == null) {
            files = new ArrayList<UploadFile>();
            handleHttpPostParams();
        }

        return files;
    }

    @Override
    public UploadFile getUploadFile(final String name) {
        return CollectionExUtil.find(getUploadFiles(), new Function<UploadFile, Boolean>() {
            @Override
            public Boolean invoke(UploadFile file) {
                return StrUtil.equals(file.getName(), name);
            }
        });
    }

    @Override
    public List<UploadFile> getUploadFiles(final String name) {
        return CollectionExUtil.findAll(getUploadFiles(), new Function<UploadFile, Boolean>() {
            @Override
            public Boolean invoke(UploadFile file) {
                return StrUtil.equals(file.getName(), name);
            }
        });
    }

    @Override
    public HttpServerSession getSession(boolean create) {
        if (create) {
            session = getSession();
        }

        return session;
    }

    @Override
    public HttpServerSession getSession() {
        if (session == null) {
            session = sessionManager.loadSessionByCookie(this);
        }

        if (session == null) {
            session = sessionManager.createSession(this);
        }

        return session;
    }

    private Map<String, List<String>> handleUrlParams() {
        return new QueryStringDecoder(getRequestURI()).parameters();
    }

    private synchronized Map<String, List<String>> handleHttpPostParams() {
        // 不允许重复处理参数
        if (httpPostRequestDecoder != null) {
            return EMPTY_POST_PARAMS;
        }

        initHttpPostRequestDecoder();

        // 无需处理参数
        if (httpPostRequestDecoder == null) {
            return EMPTY_POST_PARAMS;
        }

        Map<String, List<String>> params = new HashMap<String, List<String>>();
        for (InterfaceHttpData data : httpPostRequestDecoder.getBodyHttpDatas()) {
            switch (data.getHttpDataType()) {
                case Attribute:
                    Attribute attr = (Attribute) data;
                    try {
                        List<String> ValueUtil = getParameterMap().get(attr.getName());
                        if (ValueUtil == null) {
                            ValueUtil = new ArrayList<String>();
                        }

                        ValueUtil.add(attr.getValue());

                        getParameterMap().put(attr.getName(), ValueUtil);
                    } catch (Exception e) {
                        throw ExceptionUtil.toRuntimeException(e);
                    }
                    break;

                case FileUpload:
                    getUploadFiles().add(new NettyUploadFile((FileUpload) data));
                    break;
            }
        }

        return params;
    }

    private synchronized NettyHttpServerRequest initHttpPostRequestDecoder() {
        if (httpPostRequestDecoder == null) {
            String contentType = nettyRequest.headers().get(HttpHeaderName.CONTENT_TYPE.content());
            if (contentType != null) {
                HttpMethod method = HttpMethod.valueOf(getMethod());
                String lowerCaseContentType = contentType.toLowerCase();
                boolean isURLEncoded = lowerCaseContentType.startsWith(
                        HttpHeaderValue.APPLICATION_X_WWW_FORM_URLENCODED.content());
                if ((lowerCaseContentType.startsWith(HttpHeaderValue.MULTIPART_FORM_DATA.content()) || isURLEncoded) &&
                        (method.equals(HttpMethod.POST) || method.equals(HttpMethod.PUT) ||
                                method.equals(HttpMethod.PATCH) || method.equals(HttpMethod.DELETE))) {
                    httpPostRequestDecoder = new HttpPostRequestDecoder(
                            new MemoryAttributeHttpDataFactory(true), nettyRequest);
                }
            }
        }

        return this;
    }

    private Dict getAttributes() {
        if (attributes == null) {
            attributes = new Dict();
        }

        return attributes;
    }
}