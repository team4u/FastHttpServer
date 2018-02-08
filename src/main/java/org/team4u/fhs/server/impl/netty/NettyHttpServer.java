package org.team4u.fhs.server.impl.netty;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.team4u.fhs.server.HttpServer;
import org.team4u.fhs.server.HttpServerRequest;
import org.team4u.fhs.server.HttpServerResponse;
import org.team4u.fhs.server.impl.AbstractHttpServer;
import org.team4u.fhs.server.impl.session.SessionManager;
import org.team4u.kit.core.error.ExceptionUtil;
import org.team4u.kit.core.lang.ServiceProvider;

import javax.net.ssl.SSLContext;
import java.io.IOException;

/**
 * @author Jay Wu
 */
public class NettyHttpServer extends AbstractHttpServer {

    private static Log log = LogFactory.get();

    protected NettyHttpServerConfig config;

    protected SSLContext sslContext;
    protected Channel channel;
    protected EventLoopGroup bossGroup;
    protected EventLoopGroup workerGroup;

    public NettyHttpServer() {
        this(new NettyHttpServerConfig());
    }

    public NettyHttpServer(NettyHttpServerConfig config) {
        this(config, null);
    }

    public NettyHttpServer(NettyHttpServerConfig config, SSLContext sslContext) {
        super(config.getHttpSessionTimeoutSecs());

        this.config = config;
        this.sslContext = sslContext;
    }

    @Override
    public HttpServer listen(int port) {
        this.port = port;

        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_REUSEADDR, config.isSocketReuseAddr())
                    .option(ChannelOption.TCP_NODELAY, config.isTcpNoDelay())
                    .option(ChannelOption.SO_BACKLOG, config.getSocketBackLog())
                    .option(ChannelOption.SO_RCVBUF, config.getSocketRcvbufSize())
                    .option(ChannelOption.SO_SNDBUF, config.getSocketSndbufSize())
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_TIMEOUT, config.getSocketTimeoutMillis())
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, config.getConnectTimeoutMillis())
                    .childHandler(newChannelInitializer());

            ChannelFuture f = b.bind(port).sync();
            log.info("HttpServer started(port={})", port);

            channel = f.channel();
        } catch (Exception e) {
            log.error("HttpServer start failure(port=)" + port, e);
            throw ExceptionUtil.toRuntimeException(e);
        }

        return this;
    }

    @Override
    public void close() throws IOException {
        try {
            if (channel != null) {
                channel.close().sync();
            }
        } catch (InterruptedException e) {
            // Ignore error
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

        log.info("HttpServer closed(port={})", port);
    }

    protected WebChannelInitializer newChannelInitializer() {
        NettyHttpRequestHandler nettyHttpRequestHandler;

        if (webSocketListener != null) {
            nettyHttpRequestHandler = new NettyHttpWebSocketHandler(
                    ServiceProvider.getInstance().get(SessionManager.class), webSocketListener, config) {
                @Override
                protected void doProcess(HttpServerRequest request, HttpServerResponse response) throws Exception {
                    requestHandler.invoke(request, response);
                }
            };
        } else {
            nettyHttpRequestHandler = new NettyHttpRequestHandler(
                    ServiceProvider.getInstance().get(SessionManager.class), config) {
                @Override
                protected void doProcess(HttpServerRequest request, HttpServerResponse response) throws Exception {
                    requestHandler.invoke(request, response);
                }
            };
        }

        return new WebChannelInitializer(config, sslContext, nettyHttpRequestHandler);
    }
}