package org.team4u.fhs.server.impl.netty;

import cn.hutool.core.collection.CollUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedFile;
import org.team4u.fhs.server.HttpHeaderName;
import org.team4u.fhs.server.HttpServerRequest;
import org.team4u.fhs.server.HttpServerResponse;
import org.team4u.fhs.server.HttpServerSession;
import org.team4u.fhs.server.util.MimeUtil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class NettyHttpServerResponse implements HttpServerResponse {

    protected static final ChannelFutureListener CHANNEL_FUTURE_LISTENER = new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) {
            if (future.channel().isActive()) {
                future.channel().close();
            }
        }
    };
    protected HttpServerRequest request;
    protected Object sendFile;
    protected FullHttpRequest nettyRequest;
    protected DefaultHttpResponse nettyResponse;
    protected ChannelHandlerContext nettyChannelHandlerContext;
    protected boolean isKeepAlive = false;
    protected boolean committed = false;
    private List<org.team4u.fhs.server.Cookie> cookies;
    private long contentLength = 0;
    private ByteBuf body;

    public NettyHttpServerResponse(boolean isKeepAlive,
                                   ChannelHandlerContext nettyChannelHandlerContext,
                                   FullHttpRequest nettyRequest,
                                   HttpServerRequest request) {
        this.isKeepAlive = isKeepAlive;
        this.nettyChannelHandlerContext = nettyChannelHandlerContext;
        this.nettyRequest = nettyRequest;
        this.request = request;

        this.nettyResponse = new DefaultHttpResponse(nettyRequest.protocolVersion(), HttpResponseStatus.OK);
    }

    @Override
    public void setContentLength(long len) {
        HttpUtil.setContentLength(nettyResponse, len);
    }

    @Override
    public String getContentType() {
        return nettyResponse.headers().get(HttpHeaderName.CONTENT_TYPE.content());
    }

    @Override
    public void setContentType(String type) {
        if (type == null) {
            return;
        }

        nettyResponse.headers().set(HttpHeaderName.CONTENT_TYPE.content(), type);
    }

    public HttpServerResponse addCookie(org.team4u.fhs.server.Cookie cookie) {
        if (cookies == null) {
            cookies = new ArrayList<org.team4u.fhs.server.Cookie>();
        }

        cookies.add(cookie);
        return this;
    }

    @Override
    public boolean containsHeader(String name) {
        return nettyResponse.headers().contains(name);
    }

    @Override
    public HttpServerResponse setHeader(String name, Object value) {
        nettyResponse.headers().set(name, value);
        return this;
    }

    @Override
    public HttpServerResponse addHeader(String name, Object value) {
        nettyResponse.headers().add(name, value);
        return this;
    }

    @Override
    public HttpServerResponse setStatus(int status) {
        nettyResponse.setStatus(HttpResponseStatus.valueOf(status));
        return this;
    }

    @Override
    public HttpServerResponse sendError(int status) {
        setStatus(status);
        flush();
        return this;
    }

    @Override
    public int getStatus() {
        return nettyResponse.status().code();
    }

    @Override
    public String getHeader(String name) {
        return nettyResponse.headers().get(name);
    }

    @Override
    public Collection<String> getHeaders(String name) {
        return nettyResponse.headers().getAll(name);
    }

    @Override
    public Set<String> getHeaderNames() {
        return nettyResponse.headers().names();
    }

    public HttpServerResponse write(String data) {
        if (data != null) {
            write(data.getBytes());
        }

        return this;
    }

    @Override
    public HttpServerResponse write(byte[] bytes) {
        if (bytes == null) {
            return this;
        }

        contentLength += bytes.length;

        if (body == null) {
            body = Unpooled.buffer(bytes.length);
        }

        body.writeBytes(bytes);
        return this;
    }

    @Override
    public HttpServerResponse write(File file) {
        try {
            long fileLength = file.length();
            contentLength += fileLength;

            RandomAccessFile raf = new RandomAccessFile(file, "r");

            setContentType(MimeUtil.getMimeType(file));

            if (nettyChannelHandlerContext.pipeline().get(SslHandler.class) == null) {
                sendFile = new DefaultFileRegion(raf.getChannel(), 0L, fileLength);
            } else {
                sendFile = new ChunkedFile(raf, 0L, fileLength, 8192);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public HttpServerResponse flush() {
        synchronized (this) {
            if (committed) {
                return this;
            }

            committed = true;
        }

        // Set http header
        setContentLength(contentLength);

        if (containsHeader(HttpHeaderName.CONTENT_TYPE.content())) {
            String contentType = getContentType();
            if (!contentType.contains("charset")) {
                contentType = contentType + "; charset=UTF-8";
                setContentType(contentType);
            }
        }

        boolean isKeepAlive = this.isKeepAlive && HttpUtil.isKeepAlive(nettyRequest);
        if (isKeepAlive) {
            HttpUtil.setKeepAlive(nettyResponse, true);
        }

        // Set session cookie
        HttpServerSession session = request.getSession(false);
        if (session != null) {
            org.team4u.fhs.server.Cookie cookie = new org.team4u.fhs.server.Cookie(HttpServerSession.SESSION_ID_NAME, session.getId());
            cookie.setPath("/");
            addCookie(cookie);
        }

        // Set cookies
        if (CollUtil.isNotEmpty(cookies)) {
            for (org.team4u.fhs.server.Cookie cookie : cookies) {
                DefaultCookie nettyCookie = new DefaultCookie(cookie.getName(), cookie.getValue());
                nettyCookie.setPath(cookie.getPath());
                nettyCookie.setDomain(cookie.getDomain());
                if (cookie.getMaxAge() > -1) {
                    nettyCookie.setMaxAge(cookie.getMaxAge());
                }
                addHeader(HttpHeaderName.SET_COOKIE.content(), ServerCookieEncoder.LAX.encode(nettyCookie));
            }
        }

        // Write header
        nettyChannelHandlerContext.write(nettyResponse);

        // Write body
        if (body != null) {
            if (body.isReadable()) {
                nettyChannelHandlerContext.write(body);
            } else {
                body.release();
            }
        }

        // Write file
        if (sendFile != null) {
            nettyChannelHandlerContext.write(sendFile, nettyChannelHandlerContext.newProgressivePromise());
        }

        // Write end marker
        ChannelFuture lastContentFuture = nettyChannelHandlerContext.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

        if (!isKeepAlive) {
            lastContentFuture.addListener(CHANNEL_FUTURE_LISTENER);
        }

        return this;
    }

    @Override
    public boolean isCommitted() {
        return committed;
    }
}