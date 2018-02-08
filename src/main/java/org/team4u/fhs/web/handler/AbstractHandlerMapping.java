package org.team4u.fhs.web.handler;

import org.team4u.fhs.server.HttpServerRequest;
import org.team4u.fhs.web.HandlerExecutionChain;
import org.team4u.fhs.web.HandlerInterceptor;
import org.team4u.fhs.web.HandlerMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jay Wu
 */
public abstract class AbstractHandlerMapping implements HandlerMapping {

    private final List<HandlerInterceptor> adaptedInterceptors = new ArrayList<HandlerInterceptor>();

    @Override
    public final HandlerExecutionChain getHandler(HttpServerRequest request) throws Exception {
        Object handler = getHandlerInternal(request);

        if (handler == null) {
            return null;
        }

        return getHandlerExecutionChain(handler, request);
    }

    public AbstractHandlerMapping addLastAdaptedInterceptor(HandlerInterceptor interceptor) {
        adaptedInterceptors.add(interceptor);
        return this;
    }

    public AbstractHandlerMapping addFirstAdaptedInterceptor(HandlerInterceptor interceptor) {
        adaptedInterceptors.add(0, interceptor);
        return this;
    }

    public List<HandlerInterceptor> getAdaptedInterceptors() {
        return adaptedInterceptors;
    }

    protected HandlerExecutionChain getHandlerExecutionChain(Object handler, HttpServerRequest request) {
        HandlerExecutionChain chain = (handler instanceof HandlerExecutionChain ?
                (HandlerExecutionChain) handler : new HandlerExecutionChain(handler));

        for (HandlerInterceptor interceptor : this.adaptedInterceptors) {
            chain.addInterceptor(interceptor);
        }
        return chain;
    }

    protected abstract Object getHandlerInternal(HttpServerRequest request) throws Exception;
}