package org.team4u.fhs.server.impl.netty;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.AttributeKey;
import org.team4u.fhs.server.HttpHeaderName;
import org.team4u.fhs.server.WebSocketListener;
import org.team4u.fhs.server.WebSocketSession;
import org.team4u.fhs.server.impl.session.SessionManager;
import org.team4u.kit.core.action.Callback;
import org.team4u.kit.core.lang.TimeMap;

import java.util.Map;

/**
 * @author Jay Wu
 */
@ChannelHandler.Sharable
public abstract class NettyHttpWebSocketHandler extends NettyHttpRequestHandler {

    private static final Log log = LogFactory.get();

    private static final AttributeKey<Object> WEB_SOCKET_KEY = AttributeKey.valueOf("WEB_SOCKET_KEY");

    private static final String WEB_SOCKET_PATH = "WebSocket";

    private Map<Channel, NettyWebSocketSession> sessions;

    private WebSocketListener listener;

    public NettyHttpWebSocketHandler(SessionManager sessionManager,
                                     WebSocketListener listener,
                                     NettyHttpServerConfig config) {
        super(sessionManager, config);
        this.listener = listener;

        sessions = new TimeMap<Channel, NettyWebSocketSession>(config.getWebSocketTimeoutSecs(),
                new TimeMap.ExpiredCallback<Channel, NettyWebSocketSession>() {
                    @Override
                    public void expire(Channel channel, NettyWebSocketSession session) {
                        onWebSocketClose(session);
                    }
                });
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof WebSocketFrame) {
            handleWebSocketMessage(ctx.channel(), (WebSocketFrame) msg);
        } else {
            if (msg instanceof FullHttpRequest) {
                FullHttpRequest request = (FullHttpRequest) msg;
                if (request.headers().contains(HttpHeaderName.SEC_WEBSOCKET_VERSION.content())) {
                    createNewWebSocket(ctx, request);
                } else {
                    super.channelRead0(ctx, request);
                }
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        if (ctx.channel().attr(WEB_SOCKET_KEY).getAndRemove() != null) {
            onWebSocketClose(ctx.channel());
        }
    }

    private void createNewWebSocket(final ChannelHandlerContext ctx, final FullHttpRequest request) {
        final NettyWebSocketSession session = getWebSocketSession(ctx, request);
        if (session == null) {
            return;
        }

        String scheme = ctx.pipeline().get(SslHandler.class) == null ? "ws://" : "wss://";
        String path = scheme + request.headers().get(HttpHeaderName.HOST.content()) + WEB_SOCKET_PATH;
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(path, null, false);
        final WebSocketServerHandshaker wsHandshaker = wsFactory.newHandshaker(request);

        if (wsHandshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel())
                    .addListener(ChannelFutureListener.CLOSE);
        } else {
            wsHandshaker.handshake(ctx.channel(), request).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture f) throws Exception {
                    if (f.isSuccess()) {
                        f.channel().attr(WEB_SOCKET_KEY).set(wsHandshaker);
                        onWebSocketOpen(session);
                    } else {
                        f.channel().pipeline().fireExceptionCaught(f.cause());
                    }
                }
            });
        }
    }

    private NettyWebSocketSession getWebSocketSession(ChannelHandlerContext ctx, FullHttpRequest request) {
        NettyWebSocketSession session = new NettyWebSocketSession(request, ctx);

        if (listener.supports(session)) {
            return session;
        } else {
            if (log.isTraceEnabled()) {
                log.trace("Not support WebSocket request(uri={})", session.getRequestURI());
            }
            return null;
        }
    }

    private void onWebSocketOpen(NettyWebSocketSession session) {
        session.setOpen(true);
        session.setActiveListener(new Callback<NettyWebSocketSession>() {
            @Override
            public void invoke(NettyWebSocketSession session) {
                sessions.put(session.getNettyChannelHandlerContext().channel(), session);
            }
        });

        session.keepActive();

        if (log.isTraceEnabled()) {
            log.trace("WebSocket open({})", session);
        }

        listener.onOpen(session);
    }

    private void handleWebSocketMessage(Channel channel, WebSocketFrame frame) {
        if (frame instanceof TextWebSocketFrame) {
            onWebSocketMessage(channel, ((TextWebSocketFrame) frame).text());
            return;
        }

        if (frame instanceof BinaryWebSocketFrame) {
            BinaryWebSocketFrame b = (BinaryWebSocketFrame) frame;
            byte[] bytes = new byte[b.content().readableBytes()];
            b.content().readBytes(bytes);
            onWebSocketMessage(channel, bytes);
            return;
        }

        if (frame instanceof CloseWebSocketFrame) {
            WebSocketServerHandshaker hs = (WebSocketServerHandshaker) channel.attr(WEB_SOCKET_KEY).getAndSet(null);
            if (hs != null) {
                hs.close(channel, (CloseWebSocketFrame) frame.retain()).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        onWebSocketClose(future.channel());
                    }
                });
            }
        }
    }

    private void onWebSocketClose(Channel channel) {
        WebSocketSession session = sessions.get(channel);
        sessions.remove(channel);
        onWebSocketClose(session);
    }

    private void onWebSocketClose(WebSocketSession session) {
        if (session != null) {
            try {
                session.close();

                if (log.isTraceEnabled()) {
                    log.trace("WebSocket closed({})", session);
                }

                listener.onClose(session);
            } catch (Exception e) {
                log.error(String.format("WebSocket close error({})", session), e);
            }
        }
    }

    private void onWebSocketMessage(Channel channel, String message) {
        NettyWebSocketSession session = sessions.get(channel);
        if (session != null) {
            if (log.isTraceEnabled()) {
                log.trace("onWebSocketTextMessage(message={},session={})", message, session);
            }

            session.keepActive();
            listener.onText(message, session);
        }
    }

    private void onWebSocketMessage(Channel channel, byte[] message) {
        NettyWebSocketSession session = sessions.get(channel);
        if (session != null) {
            if (log.isTraceEnabled()) {
                log.trace("onWebSocketBinaryMessage(message={},session={})", message, session);
            }

            session.keepActive();
            listener.onBinary(message, session);
        }
    }
}