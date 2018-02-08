package org.team4u.fhs.server.impl.session;

import org.team4u.fhs.server.HttpServerRequest;
import org.team4u.fhs.server.HttpServerSession;

/**
 * @author Jay Wu
 */
public interface HttpServerSessionFactory {

    HttpServerSession create(HttpServerRequest request, int maxInactiveInterval);
}