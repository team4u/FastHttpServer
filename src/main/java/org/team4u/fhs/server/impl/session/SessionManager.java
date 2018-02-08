package org.team4u.fhs.server.impl.session;


import cn.hutool.core.util.StrUtil;
import org.team4u.fhs.server.Cookie;
import org.team4u.fhs.server.HttpServerRequest;
import org.team4u.fhs.server.HttpServerSession;
import org.team4u.kit.core.lang.ServiceProvider;

public class SessionManager {

    private int maxSessionTimeoutSec;

    private HttpServerSessionFactory sessionFactory;
    private HttpServerSessionRepository sessionRepository;

    public SessionManager(int maxSessionTimeoutSec) {
        this.maxSessionTimeoutSec = maxSessionTimeoutSec;
    }

    public HttpServerSession createSession(HttpServerRequest request) {
        HttpServerSession session = getSessionFactory().create(request, maxSessionTimeoutSec);
        getSessionRepository().save(session);

        return session;
    }

    public HttpServerSession changeSessionId(String id) {
        HttpServerSession session = getSessionRepository().load(id);

        if (session == null) {
            throw new IllegalStateException(String.format("Session invalidated(id={})", id));
        }

        getSessionRepository().remove(id);
        session.changeId();
        getSessionRepository().save(session);

        return session;
    }

    public HttpServerSession loadSessionByCookie(HttpServerRequest request) {
        if (maxSessionTimeoutSec <= 0) {
            return null;
        }

        Cookie sessionCookie = request.getCookie(HttpServerSession.SESSION_ID_NAME);

        if (sessionCookie != null) {
            String sessionId = sessionCookie.getValue();
            if (!StrUtil.isBlank(sessionId)) {
                HttpServerSession session = getSessionRepository().load(sessionId);
                if (session != null) {
                    getSessionRepository().save(session);
                    return session;
                }
            }
        }

        return null;
    }

    public HttpServerSessionRepository getSessionRepository() {
        if (sessionRepository == null) {
            sessionRepository = ServiceProvider.getInstance().get(HttpServerSessionRepository.class);
        }
        return sessionRepository;
    }

    public SessionManager setSessionRepository(HttpServerSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
        return this;
    }

    public HttpServerSessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = ServiceProvider.getInstance().get(HttpServerSessionFactory.class);
        }
        return sessionFactory;
    }

    public SessionManager setSessionFactory(HttpServerSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        return this;
    }
}