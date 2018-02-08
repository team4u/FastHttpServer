package org.team4u.fhs.server.impl.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

/**
 * @author Jay Wu
 */
public class WebChannelInitializer extends ChannelInitializer<Channel> {

    private NettyHttpServerConfig config;

    private ChannelHandler nettyHttpRequestHandler;

    private SSLContext sslContext;

    public WebChannelInitializer(NettyHttpServerConfig config,
                                 SSLContext sslContext,
                                 ChannelHandler nettyHttpRequestHandler) {
        this.config = config;
        this.nettyHttpRequestHandler = nettyHttpRequestHandler;
        this.sslContext = sslContext;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        if (sslContext != null) {
            SSLEngine sslEngine = sslContext.createSSLEngine();
            sslEngine.setUseClientMode(false);
            sslEngine.setNeedClientAuth(false);
            pipeline.addLast(new SslHandler(sslEngine));
        }

        pipeline.addLast(new HttpRequestDecoder())
                .addLast(new HttpObjectAggregator(config.maxRequestMessageSize))
                .addLast(new HttpResponseEncoder());

        if (nettyHttpRequestHandler instanceof NettyHttpWebSocketHandler) {
            pipeline.addLast(new WebSocketDataEncoder());
        }

        pipeline.addLast(nettyHttpRequestHandler);
    }
}