package org.team4u.fhs.web.handler.simple;

import org.team4u.fhs.server.HttpMethod;
import org.team4u.fhs.web.HttpRoute;
import org.team4u.fhs.web.ModelAndView;
import org.team4u.fhs.web.RoutingContext;
import org.team4u.fhs.web.handler.AbstractHttpRoute;
import org.team4u.kit.core.action.Callback;
import org.team4u.kit.core.action.Function;

/**
 * @author Jay Wu
 */
public class SimpleHttpRoute extends AbstractHttpRoute {

    private Function<RoutingContext, ModelAndView> requestHandler;

    public SimpleHttpRoute() {
    }

    public SimpleHttpRoute(HttpMethod method, String path) {
        super(method, path);
    }

    public SimpleHttpRoute(String path) {
        super(path);
    }

    public HttpRoute setRequestHandler(Function<RoutingContext, ModelAndView> requestHandler) {
        this.requestHandler = requestHandler;
        return this;
    }

    public HttpRoute setRequestHandler(final Callback<RoutingContext> requestHandler) {
        this.requestHandler = new Function<RoutingContext, ModelAndView>() {
            @Override
            public ModelAndView invoke(RoutingContext obj) {
                requestHandler.invoke(obj);
                return null;
            }
        };
        return this;
    }

    public Function<RoutingContext, ModelAndView> getRequestHandler() {
        return requestHandler;
    }
}