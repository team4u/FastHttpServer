package org.team4u.fhs.web.handler.method;

import org.team4u.fhs.server.HttpMethod;
import org.team4u.fhs.web.handler.AbstractHttpRoute;

import java.lang.reflect.Method;

/**
 * @author Jay Wu
 */
public class MethodHttpRoute extends AbstractHttpRoute {

    private Object controller;
    private Method handler;

    public MethodHttpRoute() {
    }

    public MethodHttpRoute(HttpMethod httpMethod, String path) {
        super(httpMethod, path);
    }

    public MethodHttpRoute(String path) {
        super(path);
    }

    public Method getHandler() {
        return handler;
    }

    public MethodHttpRoute setHandler(Method handler) {
        this.handler = handler;
        return this;
    }

    public Object getController() {
        return controller;
    }

    public MethodHttpRoute setController(Object controller) {
        this.controller = controller;
        return this;
    }
}