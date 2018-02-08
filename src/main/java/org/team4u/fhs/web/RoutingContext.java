package org.team4u.fhs.web;

import org.team4u.fhs.server.HttpServerRequest;
import org.team4u.fhs.server.HttpServerResponse;
import org.team4u.fhs.server.WebSocketSession;

/**
 * @author Jay Wu
 */
public class RoutingContext {

    private HttpServerRequest request;

    private HttpServerResponse response;

    private WebSocketSession webSocketSession;

    public HttpServerResponse getResponse() {
        return response;
    }

    public RoutingContext setResponse(HttpServerResponse response) {
        this.response = response;
        return this;
    }

    public HttpServerRequest getRequest() {
        return request;
    }

    public RoutingContext setRequest(HttpServerRequest request) {
        this.request = request;
        return this;
    }

    public WebSocketSession getWebSocketSession() {
        return webSocketSession;
    }

    public RoutingContext setWebSocketSession(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
        return this;
    }
}