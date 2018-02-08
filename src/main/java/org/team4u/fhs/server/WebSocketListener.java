package org.team4u.fhs.server;

/**
 * @author Jay Wu
 */
public interface WebSocketListener {
    /**
     * Whether the given session is supported by this listener.
     */
    boolean supports(WebSocketSession session);

    /**
     * A Close Event was received.
     * <p>
     * The underlying Connection will be considered closed at this point.
     */
    void onClose(WebSocketSession session);

    /**
     * A WebSocket {@link WebSocketSession} has connected successfully and is ready to be used.
     *
     * @param session the websocket session.
     */
    void onOpen(WebSocketSession session);

    /**
     * A WebSocket Text frame was received.
     */
    void onText(String message, WebSocketSession session);

    /**
     * A WebSocket binary frame has been received.
     */
    void onBinary(byte[] message, WebSocketSession session);
}