package org.team4u.fhs.server.impl.session;

import org.team4u.fhs.server.HttpServerSession;

/**
 * @author Jay Wu
 */
public interface HttpServerSessionRepository {

    void save(HttpServerSession session);

    void remove(String sessionId);

    HttpServerSession load(String sessionId);
}