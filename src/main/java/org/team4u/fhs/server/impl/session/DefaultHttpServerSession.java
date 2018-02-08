package org.team4u.fhs.server.impl.session;

import org.team4u.fhs.server.HttpServerRequest;
import org.team4u.fhs.server.HttpServerSession;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DefaultHttpServerSession implements HttpServerSession {

    private HttpServerRequest request;

    private String id;

    private Date creationTime;

    private int maxInactiveInterval;

    private Map<String, Object> cache;

    private boolean invalidated;

    public DefaultHttpServerSession(HttpServerRequest request, int maxInactiveInterval) {
        this.request = request;
        this.maxInactiveInterval = maxInactiveInterval;

        creationTime = new Date();
        cache = new HashMap<String, Object>();

        changeId();
    }

    @Override
    public long getCreationTime() {
        return creationTime.getTime();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getAttribute(String name) {
        checkInvalidated();
        return cache.get(name);
    }

    @Override
    public void setAttribute(String name, Object value) {
        checkInvalidated();
        cache.put(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        checkInvalidated();
        cache.remove(name);
    }

    @Override
    public void invalidate() {
        checkInvalidated();

        invalidated = true;
        cache.clear();
    }

    @Override
    public boolean isNew() {
        checkInvalidated();

        return request.getCookie(HttpServerSession.SESSION_ID_NAME) == null;
    }

    public String changeId() {
        id = UUID.randomUUID().toString().replace("-", "");
        return id;
    }

    private void checkInvalidated() {
        if (invalidated) {
            throw new IllegalStateException(String.format("Session invalidated(id={})", id));
        }
    }
}