package org.team4u.fhs.server.impl.netty;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.AttributeKey;
import org.team4u.fhs.server.HttpServerRequest;
import org.team4u.fhs.server.HttpServerResponse;
import org.team4u.fhs.server.HttpStatusCode;
import org.team4u.fhs.server.impl.session.SessionManager;
import org.team4u.kit.core.lang.EmptyValue;
import org.team4u.kit.core.lang.TimeMap;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Jay Wu
 */
@ChannelHandler.Sharable
public abstract class NettyHttpRequestHandler extends SimpleChannelInboundHandler<Object> {

    private static final Log log = LogFactory.get();

    private static final AttributeKey<Object> CONNECTION_COUNTED_MARK = AttributeKey.valueOf("CONNECTION_COUNTED_MARK");

    protected Map<Channel, String> keepAliveChannels;

    protected NettyHttpServerConfig config;

    protected AtomicInteger connectionCounter;

    protected SessionManager sessionManager;

    public NettyHttpRequestHandler(SessionManager sessionManager, NettyHttpServerConfig config) {
        this.sessionManager = sessionManager;
        connectionCounter = new AtomicInteger();
        this.config = config;

        keepAliveChannels = new TimeMap<Channel, String>(config.getHttpKeepAliveTimeoutSecs(),
                new TimeMap.ExpiredCallback<Channel, String>() {
                    @Override
                    public void expire(Channel channel, String nothing) {
                        try {
                            if (channel.isActive()) {
                                channel.close();
                            }
                        } catch (Exception e) {
                            // Ignore error
                        }
                    }
                });
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;

            if (!request.decoderResult().isSuccess()) {
                DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                        request.protocolVersion(), HttpResponseStatus.INTERNAL_SERVER_ERROR);
                HttpUtil.setKeepAlive(response, false);
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                return;
            }

            boolean isKeepAlive = config.isHttpKeepAlive() && HttpUtil.isKeepAlive(request);
            HttpServerRequest httpServerRequest = new NettyHttpServerRequest(sessionManager, ctx, request);
            HttpServerResponse httpServerResponse = new NettyHttpServerResponse(isKeepAlive, ctx, request, httpServerRequest);

            try {
                doProcess(httpServerRequest, httpServerResponse);

                if (!httpServerResponse.isCommitted()) {
                    httpServerResponse.flush();
                }
            } catch (Throwable e) {
                log.error(e, "Handle request error(request={})", httpServerRequest.getRequestURI());
                httpServerResponse.sendError(HttpStatusCode.INTERNAL_SERVER_ERROR.code());
            } finally {
                keepAlive(isKeepAlive, ctx.channel());
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        boolean ignoreThrowable = false;

        // 客户端提前关闭连接,忽略这种错误
        if (cause instanceof IOException) {
            if (cause.getMessage().equals("Connection reset by peer")) {
                ignoreThrowable = true;
            }
        }

        if (!ignoreThrowable) {
            log.error(cause, "Handle request error(ctx={})", ctx);
        }

        keepAliveChannels.remove(ctx.channel());
        ctx.channel().close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (config.getMaxActiveConnection() > 0 && connectionCounter.get() > config.getMaxActiveConnection()) {
            ctx.close();
            return;
        }

        super.channelActive(ctx);

        connectionCounter.incrementAndGet();
        ctx.channel().attr(CONNECTION_COUNTED_MARK).set(1);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Object counted = ctx.channel().attr(CONNECTION_COUNTED_MARK).getAndRemove();
        if (counted != null) {
            connectionCounter.decrementAndGet();
        }

        keepAliveChannels.remove(ctx.channel());
        super.channelInactive(ctx);
    }

    private void keepAlive(boolean isKeepAlive, Channel channel) {
        if (isKeepAlive && channel.isActive()) {
            keepAliveChannels.put(channel, EmptyValue.EMPTY_STRING);
        }
    }

    /**
     * 当前保持的连接数
     */
    public int getActiveConnectionSize() {
        return connectionCounter.get();
    }

    protected abstract void doProcess(HttpServerRequest request, HttpServerResponse response) throws Exception;
}