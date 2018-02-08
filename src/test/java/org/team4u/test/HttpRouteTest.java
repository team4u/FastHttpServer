package org.team4u.test;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.fhs.server.Cookie;
import org.team4u.fhs.server.HttpHeaderValue;
import org.team4u.fhs.server.HttpMethod;
import org.team4u.fhs.server.HttpServerRequest;
import org.team4u.fhs.server.impl.mock.MockHttpServerRequest;
import org.team4u.fhs.server.impl.mock.MockHttpServerResponse;
import org.team4u.fhs.web.*;
import org.team4u.fhs.web.ext.view.FastJsonViewResolver;
import org.team4u.fhs.web.handler.method.RequestMappingHandlerMapping;
import org.team4u.fhs.web.handler.simple.FileRuleHttpRouteHandlerMapping;
import org.team4u.fhs.web.handler.simple.SimpleHttpRoute;
import org.team4u.fhs.web.handler.simple.SimpleHttpRouteHandlerMapping;
import org.team4u.kit.core.action.Callback;
import org.team4u.kit.core.action.Function;

import java.util.List;

/**
 * @author Jay Wu
 */
public class HttpRouteTest {

    @Test
    public void matches() {
        SimpleHttpRoute route = new SimpleHttpRoute();
        route.path("/:id");

        MockHttpServerRequest request = new MockHttpServerRequest();
        request.setPath("/xxx");
        boolean result = route.matches(request);
        Assert.assertEquals("xxx", request.getParameter("id"));
        Assert.assertTrue(result);

        request.setPath("/xxx/");
        result = route.matches(request);
        Assert.assertEquals("xxx", request.getParameter("id"));
        Assert.assertTrue(result);

        request.setPath("/xxx/y");
        result = route.matches(request);
        Assert.assertFalse(result);

        request.setPath("/xxx/y/xxx");
        result = route.matches(request);
        Assert.assertFalse(result);
    }

    @Test
    public void doRouteWithFileSimpleHttpRoute() {
        HandlerMapping mapping = new FileRuleHttpRouteHandlerMapping().addRouteRuleFile("mock/mock.json");
        testDoRoute(new DefaultHttpRouter().addLastHandlerMapping(mapping));
    }

    @Test
    public void doRouteWithSimpleHttpRoute() {
        HandlerMapping mapping = new SimpleHttpRouteHandlerMapping()
                .addRoute(new SimpleHttpRoute()
                        .setRequestHandler(new Function<RoutingContext, ModelAndView>() {
                            @Override
                            public ModelAndView invoke(RoutingContext context) {
                                Dict model = new Dict()
                                        .set("result", context.getRequest().getParameter("x") +
                                                context.getRequest().getHeader("y") +
                                                context.getRequest().getCookie("z").getValue());
                                return new ModelAndView("json", model);
                            }
                        }).path("/test/j"))
                .addRoute(new SimpleHttpRoute()
                        .setRequestHandler(new Callback<RoutingContext>() {
                            @Override
                            public void invoke(RoutingContext context) {
                                context.getResponse().setContentType(HttpHeaderValue.TEXT_PLAIN.content());
                                context.getResponse().write(context.getRequest().getSession().getId());
                            }
                        }).path("/test/session/*"))
                .addRoute(new SimpleHttpRoute()
                        .setRequestHandler(new Callback<RoutingContext>() {
                            @Override
                            public void invoke(RoutingContext context) {
                                String x = context.getRequest().getParameter("x");
                                String y = context.getRequest().getParameter("y");
                                context.getResponse().write(x + y);
                            }
                        }).path("/test/rest/:x/:y"))
                .addRoute(new SimpleHttpRoute()
                        .setRequestHandler(new Function<RoutingContext, ModelAndView>() {
                            @Override
                            public ModelAndView invoke(RoutingContext context) {
                                List<TestController.User> list = JSON.parseObject(
                                        context.getRequest().getBodyString(),
                                        new TypeReference<List<TestController.User>>() {
                                        }.getType());
                                return new ModelAndView("json", list);
                            }
                        }).path("/test/body").httpMethod(HttpMethod.POST))
                .addRoute(new SimpleHttpRoute()
                        .setRequestHandler(new Function<RoutingContext, ModelAndView>() {
                            @Override
                            public ModelAndView invoke(RoutingContext context) {
                                return new ModelAndView("raw", context.getRequest().getParameter("name"));
                            }
                        }).path("/test/model").httpMethod(HttpMethod.GET));

        testDoRoute(new DefaultHttpRouter().addLastHandlerMapping(mapping));
    }

    @Test
    public void doRouteWithController() {
        HandlerMapping mapping = new RequestMappingHandlerMapping()
                .addController(new TestController());

        testDoRoute(new DefaultHttpRouter().addLastHandlerMapping(mapping));
    }

    private void testDoRoute(DefaultHttpRouter router) {
        router.addLastViewResolver(new FastJsonViewResolver());

        RouterWorker worker = new RouterWorker(router);
        MockHttpServerRequest request = new MockHttpServerRequest().setPath("/test/rest/1/2");
        MockHttpServerResponse response = worker.doRoute(request);
        Assert.assertEquals("12", new String(response.getBody()));

        request = new MockHttpServerRequest()
                .setPath("/test/body")
                .setMethod(HttpMethod.POST.name())
                .setBodyString(JSON.toJSONString(CollUtil.newArrayList(new TestController.User("jay"))));
        response = worker.doRoute(request);
        Assert.assertEquals(HttpHeaderValue.APPLICATION_JSON.content(), response.getContentType());
        Assert.assertEquals("[{\"name\":\"jay\"}]", new String(response.getBody()));

        request = new MockHttpServerRequest().setPath("/test/session/any");
        response = worker.doRoute(request);
        Assert.assertEquals(HttpHeaderValue.TEXT_PLAIN.content(), response.getContentType());
        Assert.assertEquals(32, new String(response.getBody()).length());

        request = new MockHttpServerRequest().setPath("/test/j");
        request.getParameterMap().put("x", CollUtil.newArrayList("1"));
        request.getCookies().add(new Cookie("z", "3"));
        request.getHeaders().put("y", "2");
        response = worker.doRoute(request);
        Assert.assertEquals("{\"result\":\"123\"}", new String(response.getBody()));
        Assert.assertEquals(HttpHeaderValue.APPLICATION_JSON.content(), response.getContentType());

        request = new MockHttpServerRequest();
        request.setPath("/test/model");
        request.getParameterMap().put("name", CollUtil.newArrayList("fjay"));
        response = worker.doRoute(request);
        Assert.assertEquals("fjay", new String(response.getBody()));
        Assert.assertEquals(HttpHeaderValue.TEXT_PLAIN.content(), response.getContentType());
    }

    private class RouterWorker {
        private HttpRouter router;

        RouterWorker(HttpRouter router) {
            this.router = router;
        }

        MockHttpServerResponse doRoute(HttpServerRequest request) {
            MockHttpServerResponse response = new MockHttpServerResponse();
            router.doRoute(new RoutingContext()
                    .setRequest(request)
                    .setResponse(response));
            return response;
        }
    }
}