package org.team4u.fhs.server.impl.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * @author Jay Wu
 */
class WebSocketDataEncoder extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        try {
            if (msg instanceof WebSocketFrame) {
                ctx.write(msg, promise);
            } else if (msg instanceof byte[]) {
                BinaryWebSocketFrame f = new BinaryWebSocketFrame(Unpooled.wrappedBuffer((byte[]) msg));
                ctx.write(f, promise);
            } else if (msg instanceof CharSequence) {
                ctx.write(new TextWebSocketFrame(msg.toString()), promise);
            } else {
                ctx.write(msg, promise);
            }
        } catch (EncoderException ee) {
            throw ee;
        } catch (Exception e) {
            throw new EncoderException(e);
        }
    }
}