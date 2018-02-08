package org.team4u.fhs.web.handler.simple;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.fhs.server.HttpServerRequest;
import org.team4u.fhs.web.HttpRoute;
import org.team4u.fhs.web.handler.AbstractHandlerMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jay Wu
 */
public class SimpleHttpRouteHandlerMapping extends AbstractHandlerMapping {

    private static final Log log = LogFactory.get();

    protected final List<HttpRoute> routes = new ArrayList<HttpRoute>();

    public SimpleHttpRouteHandlerMapping addRoute(HttpRoute route) {
        routes.add(0, route);

        if (log.isTraceEnabled()) {
            log.trace("Added HttpRoute({})", route);
        }
        return this;
    }

    @Override
    protected Object getHandlerInternal(HttpServerRequest request) throws Exception {
        for (HttpRoute httpRoute : routes) {
            if (httpRoute.matches(request)) {
                if (log.isTraceEnabled()) {
                    log.trace("HttpRoute matches the request(requestPath={},{})", request.getPath(), httpRoute);
                }
                return httpRoute;
            }
        }

        return null;
    }
}