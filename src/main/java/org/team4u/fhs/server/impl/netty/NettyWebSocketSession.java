package org.team4u.fhs.server.impl.netty;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.ssl.SslHandler;
import org.team4u.fhs.server.HttpHeaderName;
import org.team4u.fhs.server.WebSocketSession;
import org.team4u.kit.core.action.Callback;
import org.team4u.kit.core.codec.CodecRegistry;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @author Jay Wu
 */
public class NettyWebSocketSession implements WebSocketSession {

    protected URI uri;
    protected Map<String, List<String>> params;
    protected Dict attributes;
    private FullHttpRequest nettyRequest;
    private ChannelHandlerContext nettyChannelHandlerContext;
    private InetSocketAddress localAddress;
    private InetSocketAddress remoteAddress;
    private boolean secure;
    private String protocolVersion;
    private long idleTimeout;
    private boolean open;

    private Callback<NettyWebSocketSession> activeListener;

    public NettyWebSocketSession(FullHttpRequest request, ChannelHandlerContext ctx) {
        this.nettyRequest = request;
        this.nettyChannelHandlerContext = ctx;

        this.protocolVersion = request.headers().get(HttpHeaderName.SEC_WEBSOCKET_VERSION.content());
        this.secure = ctx.pipeline().get(SslHandler.class) == null;
        remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        localAddress = (InetSocketAddress) ctx.channel().localAddress();

        try {
            uri = new URI(nettyRequest.uri());
        } catch (URISyntaxException e) {
            // Ignore error
        }
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
    public NettyWebSocketSession setAttribute(String name, Object o) {
        getAttributes().put(name, o);
        return this;
    }

    @Override
    public NettyWebSocketSession removeAttribute(String name) {
        getAttributes().remove(name);
        return this;
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
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(getRequestURI());
            params = new LinkedHashMap<String, List<String>>();
            params.putAll(queryStringDecoder.parameters());
        }

        return params;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }

    @Override
    public String getProtocolVersion() {
        return protocolVersion;
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
    public String getRequestURI() {
        return nettyRequest.uri();
    }

    @Override
    public String getPath() {
        return uri.getPath();
    }

    @Override
    public String getQueryString() {
        return uri.getQuery();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    public NettyWebSocketSession setOpen(boolean open) {
        this.open = open;
        return this;
    }

    @Override
    public boolean isSecure() {
        return secure;
    }

    public NettyWebSocketSession keepActive() {
        activeListener.invoke(this);
        return this;
    }

    @Override
    public void write(Object message) {
        if (!open) {
            return;
        }

        keepActive();

        nettyChannelHandlerContext.writeAndFlush(message);
    }

    @Override
    public synchronized void close() throws IOException {
        if (!open) {
            return;
        }

        open = false;
        nettyChannelHandlerContext.close();
    }

    public FullHttpRequest getNettyRequest() {
        return nettyRequest;
    }

    public ChannelHandlerContext getNettyChannelHandlerContext() {
        return nettyChannelHandlerContext;
    }

    private Dict getAttributes() {
        if (attributes == null) {
            attributes = new Dict();
        }

        return attributes;
    }

    private Map<String, String> handleUrlParams(String queryString) {
        return CodecRegistry.URL_TO_MAP_CODEC.encode(queryString);
    }

    public Callback<NettyWebSocketSession> getActiveListener() {
        return activeListener;
    }

    public NettyWebSocketSession setActiveListener(Callback<NettyWebSocketSession> activeListener) {
        this.activeListener = activeListener;
        return this;
    }

    @Override
    public String toString() {
        return "NettyWebSocketSession{" +
                ", open=" + open +
                ", idleTimeout=" + idleTimeout +
                ", remoteAddress=" + remoteAddress +
                ", uri=" + uri +
                '}';
    }
}