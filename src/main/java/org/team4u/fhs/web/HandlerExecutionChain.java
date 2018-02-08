package org.team4u.fhs.web;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Handler execution chain, consisting of handler object and any handler interceptors.
 * Returned by HandlerMapping's {@link HandlerMapping#getHandler} method.
 *
 * @author Jay Wu
 */
public class HandlerExecutionChain {

    private static final Log log = LogFactory.get();

    private final Object handler;

    private List<HandlerInterceptor> interceptorList = new ArrayList<HandlerInterceptor>();

    private int interceptorIndex = -1;

    /**
     * Create a new HandlerExecutionChain.
     *
     * @param handler the handler object to execute
     */
    public HandlerExecutionChain(Object handler) {
        this(handler, (HandlerInterceptor[]) null);
    }

    /**
     * Create a new HandlerExecutionChain.
     *
     * @param handler      the handler object to execute
     * @param interceptors the array of interceptors to apply
     *                     (in the given order) before the handler itself executes
     */
    public HandlerExecutionChain(Object handler, HandlerInterceptor... interceptors) {
        if (handler instanceof HandlerExecutionChain) {
            HandlerExecutionChain originalChain = (HandlerExecutionChain) handler;
            this.handler = originalChain.getHandler();
            this.interceptorList.addAll(originalChain.interceptorList);
        } else {
            this.handler = handler;
        }

        this.interceptorList.addAll(CollUtil.newArrayList(interceptors));
    }


    /**
     * Return the handler object to execute.
     *
     * @return the handler object
     */
    public Object getHandler() {
        return this.handler;
    }

    public void addInterceptor(HandlerInterceptor interceptor) {
        interceptorList.add(interceptor);
    }

    public void addInterceptors(HandlerInterceptor... interceptors) {
        if (!ArrayUtil.isEmpty(interceptors)) {
            interceptorList.addAll(Arrays.asList(interceptors));
        }
    }

    public List<HandlerInterceptor> getInterceptorList() {
        return interceptorList;
    }

    /**
     * Apply preHandle methods of registered interceptors.
     *
     * @return {@code true} if the execution chain should proceed with the
     * next interceptor or the handler itself. Else, Dispatcher assumes
     * that this interceptor has already dealt with the response itself.
     */
    boolean applyPreHandle(RoutingContext context) throws Exception {
        for (int i = 0; i < interceptorList.size(); i++) {
            HandlerInterceptor interceptor = interceptorList.get(i);
            if (!interceptor.preHandle(context, this.handler)) {
                triggerAfterCompletion(context, null);
                return false;
            }
            this.interceptorIndex = i;
        }
        return true;
    }

    /**
     * Apply postHandle methods of registered interceptors.
     */
    void applyPostHandle(RoutingContext context, ModelAndView mv) throws Exception {
        for (int i = interceptorList.size() - 1; i >= 0; i--) {
            HandlerInterceptor interceptor = interceptorList.get(i);
            interceptor.postHandle(context, this.handler, mv);
        }
    }

    /**
     * Trigger afterCompletion callbacks on the mapped HandlerInterceptors.
     * Will just invoke afterCompletion for all interceptors whose preHandle invocation
     * has successfully completed and returned true.
     */
    void triggerAfterCompletion(RoutingContext context, Exception ex)
            throws Exception {
        for (int i = this.interceptorIndex; i >= 0; i--) {
            HandlerInterceptor interceptor = interceptorList.get(i);
            try {
                interceptor.afterCompletion(context, this.handler, ex);
            } catch (Throwable ex2) {
                log.error("HandlerInterceptor.afterCompletion threw exception", ex2);
            }
        }
    }

    /**
     * Delegates to the handler's {@code toString()}.
     */
    @Override
    public String toString() {
        if (this.handler == null) {
            return "HandlerExecutionChain with no handler";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("HandlerExecutionChain with handler [").append(this.handler).append("]");
        if (!CollUtil.isEmpty(this.interceptorList)) {
            sb.append(" and ").append(this.interceptorList.size()).append(" interceptor");
            if (this.interceptorList.size() > 1) {
                sb.append("s");
            }
        }
        return sb.toString();
    }
}