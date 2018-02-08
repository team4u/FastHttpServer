package org.team4u.fhs.web;

import org.team4u.fhs.server.HttpServerRequest;

/**
 * @author Jay Wu
 */
public interface HttpRouter {
    /**
     * Name of request attribute that exposes an Exception resolved with an
     * {@link HandlerExceptionResolver} but where no view was rendered
     * (e.g. setting the status code).
     */
    String EXCEPTION_ATTRIBUTE = DefaultHttpRouter.class.getName() + ".EXCEPTION";

    boolean accept(HttpServerRequest request);

    void doRoute(RoutingContext context);
}
