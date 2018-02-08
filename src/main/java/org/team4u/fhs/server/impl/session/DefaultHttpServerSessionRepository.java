package org.team4u.fhs.server.impl.session;

import org.team4u.fhs.server.HttpServerSession;
import org.team4u.kit.core.lang.TimeMap;

/**
 * @author Jay Wu
 */
public class DefaultHttpServerSessionRepository implements HttpServerSessionRepository {

    private TimeMap<String, HttpServerSession> cache;

    @Override
    public void save(HttpServerSession session) {
        if (cache == null) {
            synchronized (this) {
                if (cache == null) {
                    cache = new TimeMap<String, HttpServerSession>(session.getMaxInactiveInterval());
                }
            }
        }

        cache.put(session.getId(), session);
    }

    @Override
    public void remove(String sessionId) {
        if (cache == null) {
            return;
        }

        cache.remove(sessionId);
    }

    @Override
    public HttpServerSession load(String sessionId) {
        if (cache == null) {
            return null;
        }

        return cache.get(sessionId);
    }
}