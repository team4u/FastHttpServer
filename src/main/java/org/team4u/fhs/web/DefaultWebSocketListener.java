package org.team4u.fhs.web;

import org.team4u.fhs.server.WebSocketListener;
import org.team4u.fhs.server.WebSocketSession;
import org.team4u.fhs.web.util.WebSocketAdapterRequest;
import org.team4u.fhs.web.util.WebSocketAdapterResponse;

/**
 * @author Jay Wu
 */
public class DefaultWebSocketListener implements WebSocketListener {

    private HttpRouter router;

    public DefaultWebSocketListener(HttpRouter router) {
        this.router = router;
    }

    @Override
    public boolean supports(WebSocketSession session) {
        return router.accept(new WebSocketAdapterRequest("/onWebSocketOpen", session));
    }

    @Override
    public void onClose(WebSocketSession session) {
        router.doRoute(new RoutingContext()
                .setRequest(new WebSocketAdapterRequest("/onWebSocketClose", session))
                .setResponse(new WebSocketAdapterResponse(session))
                .setWebSocketSession(session)
        );
    }

    @Override
    public void onOpen(WebSocketSession session) {
        router.doRoute(new RoutingContext()
                .setRequest(new WebSocketAdapterRequest("/onWebSocketOpen", session))
                .setResponse(new WebSocketAdapterResponse(session))
                .setWebSocketSession(session)
        );
    }

    @Override
    public void onText(String message, WebSocketSession session) {
        router.doRoute(new RoutingContext()
                .setRequest(new WebSocketAdapterRequest("/onWebSocketText", message, session))
                .setResponse(new WebSocketAdapterResponse(session))
                .setWebSocketSession(session)
        );
    }

    @Override
    public void onBinary(byte[] message, WebSocketSession session) {
        router.doRoute(new RoutingContext()
                .setRequest(new WebSocketAdapterRequest("/onWebSocketBinary", message, session))
                .setResponse(new WebSocketAdapterResponse(session))
                .setWebSocketSession(session)
        );
    }
}