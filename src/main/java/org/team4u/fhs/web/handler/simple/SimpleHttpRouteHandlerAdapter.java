package org.team4u.fhs.web.handler.simple;

import org.team4u.fhs.web.HandlerAdapter;
import org.team4u.fhs.web.ModelAndView;
import org.team4u.fhs.web.RoutingContext;

/**
 * @author Jay Wu
 */
public class SimpleHttpRouteHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof SimpleHttpRoute;
    }

    @Override
    public ModelAndView handle(RoutingContext context, Object handler) throws Exception {
        SimpleHttpRoute route = (SimpleHttpRoute) handler;
        return route.getRequestHandler().invoke(context);
    }
}