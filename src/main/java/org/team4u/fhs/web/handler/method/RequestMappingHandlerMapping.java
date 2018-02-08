package org.team4u.fhs.web.handler.method;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.fhs.server.HttpMethod;
import org.team4u.fhs.web.handler.method.annotation.Controller;
import org.team4u.fhs.web.handler.method.annotation.RequestMapping;
import org.team4u.fhs.web.handler.simple.SimpleHttpRouteHandlerMapping;
import org.team4u.fhs.web.util.PathUtil;
import org.team4u.kit.core.util.ValueUtil;

import java.lang.reflect.Method;

/**
 * @author Jay Wu
 */
public class RequestMappingHandlerMapping extends SimpleHttpRouteHandlerMapping {

    private static final Log log = LogFactory.get();

    public RequestMappingHandlerMapping addControllerPackage(String packagePath) {
        for (Class<?> controllerClass : ClassUtil.scanPackage(packagePath)) {
            if (controllerClass.isAnnotationPresent(Controller.class)) {
                addController(ReflectUtil.newInstance(controllerClass));
            }
        }

        return this;
    }

    public RequestMappingHandlerMapping addController(Object controller) {
        if (log.isDebugEnabled()) {
            log.debug("Adding Controller(class={})", controller.getClass().getName());
        }

        Controller controllerAnnotation = controller.getClass().getAnnotation(Controller.class);
        Assert.notNull(controllerAnnotation,
                String.format("Class don't have a @Controller annotation(class={})",
                        controller.getClass().getName()));

        String rootPath = PathUtil.normalisePath(ValueUtil.defaultIfEmpty(controllerAnnotation.value(),
                StrUtil.lowerFirst(controller.getClass().getSimpleName())));

        for (Method method : controller.getClass().getDeclaredMethods()) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            if (requestMapping == null) {
                continue;
            }

            String path = PathUtil.normalisePath(ValueUtil.defaultIfEmpty(
                    requestMapping.value(), method.getName()));
            MethodHttpRoute route = new MethodHttpRoute(rootPath + path);

            for (String consume : requestMapping.consumes()) {
                route.consumes(consume);
            }

            for (String produce : requestMapping.produces()) {
                route.produces(produce);
            }

            for (HttpMethod httpMethod : requestMapping.method()) {
                route.httpMethod(httpMethod);
            }

            route.setHandler(method);
            route.setController(controller);
            addRoute(route);
        }

        if (routes.isEmpty()) {
            log.warn("Class don't have any @RequestMapping annotation on method(class={})",
                    controller.getClass().getName());
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Added Controller(class={},path={})", controller.getClass().getName(), rootPath);
            }
        }

        return this;
    }
}