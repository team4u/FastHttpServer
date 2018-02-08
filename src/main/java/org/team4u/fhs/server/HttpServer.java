package org.team4u.fhs.server;

import org.team4u.kit.core.action.Callback2;

import java.io.Closeable;

/**
 * @author Jay Wu
 */
public interface HttpServer extends Closeable {

    HttpServer onRequest(Callback2<HttpServerRequest, HttpServerResponse> handler);

    HttpServer setWebSocketListener(WebSocketListener listener);

    HttpServer listen(int port);

    /**
     * The actual port the server is listening on. This is useful if you bound the server specifying 0 as port number
     * signifying an ephemeral port
     *
     * @return the actual port the server is listening on.
     */
    int getPort();
}