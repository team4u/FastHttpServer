package org.team4u.fhs.server.impl;

import org.team4u.fhs.server.*;
import org.team4u.fhs.server.impl.session.*;
import org.team4u.kit.core.action.Callback2;
import org.team4u.kit.core.lang.ServiceProvider;

/**
 * @author Jay Wu
 */
public abstract class AbstractHttpServer implements HttpServer {

    protected Callback2<HttpServerRequest, HttpServerResponse> requestHandler;
    protected WebSocketListener webSocketListener;

    protected int port;

    protected AbstractHttpServer(int maxInactiveSessionInterval) {
        ServiceProvider.getInstance().register(HttpServerSessionFactory.class, new HttpServerSessionFactory() {
            @Override
            public HttpServerSession create(HttpServerRequest request, int maxInactiveInterval) {
                return new DefaultHttpServerSession(request, maxInactiveInterval);
            }
        });

        ServiceProvider.getInstance().register(HttpServerSessionRepository.class, new DefaultHttpServerSessionRepository());
        ServiceProvider.getInstance().register(SessionManager.class, new SessionManager(maxInactiveSessionInterval));
    }

    @Override
    public HttpServer onRequest(Callback2<HttpServerRequest, HttpServerResponse> requestHandler) {
        this.requestHandler = requestHandler;
        return this;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public HttpServer setWebSocketListener(WebSocketListener listener) {
        webSocketListener = listener;
        return this;
    }
}