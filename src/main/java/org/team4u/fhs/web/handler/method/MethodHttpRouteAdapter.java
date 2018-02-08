package org.team4u.fhs.web.handler.method;

import org.team4u.fhs.server.HttpServerRequest;
import org.team4u.fhs.web.HandlerAdapter;
import org.team4u.fhs.web.ModelAndView;
import org.team4u.fhs.web.RoutingContext;
import org.team4u.fhs.web.handler.method.support.HandlerMethodArgumentResolver;
import org.team4u.fhs.web.handler.method.support.HandlerMethodReturnValueHandler;
import org.team4u.kit.core.lang.EmptyValue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Jay Wu
 */
public class MethodHttpRouteAdapter implements HandlerAdapter {

    private List<HandlerMethodArgumentResolver> handlerMethodArgumentResolvers =
            new ArrayList<HandlerMethodArgumentResolver>();

    private List<HandlerMethodReturnValueHandler> handlerMethodReturnValueHandlers =
            new ArrayList<HandlerMethodReturnValueHandler>();

    private Map<Method, MethodInfo> methodInfos = new HashMap<Method, MethodInfo>();

    public MethodHttpRouteAdapter() {
        for (HandlerMethodArgumentResolver resolver : ServiceLoader.load(HandlerMethodArgumentResolver.class)) {
            addLastHandlerMethodArgumentResolver(resolver);
        }

        for (HandlerMethodReturnValueHandler handler : ServiceLoader.load(HandlerMethodReturnValueHandler.class)) {
            addLastHandlerMethodReturnValueHandler(handler);
        }
    }

    @Override
    public boolean supports(Object handler) {
        return handler instanceof MethodHttpRoute;
    }

    @Override
    public ModelAndView handle(RoutingContext context, Object handler) throws Exception {
        MethodHttpRoute route = (MethodHttpRoute) handler;
        Object returnValue;

        try {
            returnValue = route.getHandler().invoke(
                    route.getController(),
                    processMethodParameters(route.getHandler(), context)
            );

            if (returnValue instanceof ModelAndView) {
                return (ModelAndView) returnValue;
            }
        } catch (InvocationTargetException e) {
            returnValue = e.getCause();
        } catch (Exception e) {
            returnValue = e;
        }

        ModelAndView mv = processHandleResult(route.getHandler(), returnValue, context.getRequest());

        if (mv == null && returnValue instanceof Exception) {
            throw (Exception) returnValue;
        }

        return mv;
    }

    protected Object[] processMethodParameters(Method method, RoutingContext context) throws Exception {
        MethodInfo methodInfo = getMethodInfo(method);
        Object[] methodArgs = new Object[methodInfo.getParameterTypes().length];

        for (int i = 0; i < methodInfo.getParameterTypes().length; i++) {
            MethodParameter methodParameter = methodInfo.getMethodParameter(i);

            for (HandlerMethodArgumentResolver resolver : handlerMethodArgumentResolvers) {
                if (resolver.supportsParameter(methodParameter)) {
                    methodArgs[i] = resolver.resolveArgument(methodParameter, context);
                }
            }
        }

        return methodArgs;
    }

    protected ModelAndView processHandleResult(Method method, Object returnValue, HttpServerRequest request)
            throws Exception {
        MethodInfo methodInfo = getMethodInfo(method);
        MethodParameter returnParameter = methodInfo.getMethodParameter(-1);

        for (HandlerMethodReturnValueHandler handler : handlerMethodReturnValueHandlers) {
            if (handler.supportsReturnType(returnParameter)) {
                ModelAndView mv = handler.handleReturnValue(returnValue, returnParameter, request);

                if (mv != null) {
                    return mv;
                }
            }
        }

        return null;
    }

    public MethodHttpRouteAdapter addFirstHandlerMethodArgumentResolver(HandlerMethodArgumentResolver resolver) {
        handlerMethodArgumentResolvers.add(0, resolver);
        return this;
    }

    public MethodHttpRouteAdapter addLastHandlerMethodArgumentResolver(HandlerMethodArgumentResolver resolver) {
        handlerMethodArgumentResolvers.add(resolver);
        return this;
    }

    public MethodHttpRouteAdapter addFirstHandlerMethodReturnValueHandler(HandlerMethodReturnValueHandler resolver) {
        handlerMethodReturnValueHandlers.add(0, resolver);
        return this;
    }

    public MethodHttpRouteAdapter addLastHandlerMethodReturnValueHandler(HandlerMethodReturnValueHandler resolver) {
        handlerMethodReturnValueHandlers.add(resolver);
        return this;
    }

    public List<HandlerMethodArgumentResolver> getHandlerMethodArgumentResolvers() {
        return handlerMethodArgumentResolvers;
    }

    public MethodHttpRouteAdapter setHandlerMethodArgumentResolvers(List<HandlerMethodArgumentResolver> handlerMethodArgumentResolvers) {
        this.handlerMethodArgumentResolvers = handlerMethodArgumentResolvers;
        return this;
    }

    public List<HandlerMethodReturnValueHandler> getHandlerMethodReturnValueHandlers() {
        return handlerMethodReturnValueHandlers;
    }

    public MethodHttpRouteAdapter setHandlerMethodReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlerMethodReturnValueHandlers) {
        this.handlerMethodReturnValueHandlers = handlerMethodReturnValueHandlers;
        return this;
    }

    private MethodInfo getMethodInfo(Method method) {
        MethodInfo methodInfo = methodInfos.get(method);
        if (methodInfo != null) {
            return methodInfo;
        }

        synchronized (this) {
            methodInfo = methodInfos.get(method);
            if (methodInfo != null) {
                return methodInfo;
            }

            methodInfo = new MethodInfo(method);
            methodInfos.put(method, methodInfo);
        }

        return methodInfo;
    }

    protected static class MethodInfo {

        Map<Integer, MethodParameter> methodParameters = new HashMap<Integer, MethodParameter>();
        private Method method;
        private Class<?>[] parameterTypes;

        public MethodInfo(Method method) {
            this.method = method;
            parameterTypes = method.getParameterTypes();

            if (parameterTypes == null) {
                parameterTypes = EmptyValue.EMPTY_CLASS_ARRAY;
            }
        }

        public MethodParameter getMethodParameter(int index) {
            MethodParameter methodParameter = methodParameters.get(index);
            if (methodParameter != null) {
                return methodParameter;
            }

            synchronized (this) {
                methodParameter = methodParameters.get(index);
                if (methodParameter != null) {
                    return methodParameter;
                }

                methodParameter = new MethodParameter(method, index);
                methodParameters.put(index, methodParameter);
            }

            return methodParameter;
        }

        public Method getMethod() {
            return method;
        }

        public Class<?>[] getParameterTypes() {
            return parameterTypes;
        }
    }
}