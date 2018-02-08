package org.team4u.test;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.fhs.server.*;
import org.team4u.fhs.server.impl.netty.NettyHttpServer;
import org.team4u.fhs.server.impl.netty.NettyHttpServerConfig;
import org.team4u.fhs.server.util.MimeUtil;
import org.team4u.fhs.web.*;
import org.team4u.fhs.web.ext.view.BeetlViewResolver;
import org.team4u.fhs.web.ext.view.FastJsonViewResolver;
import org.team4u.fhs.web.ext.view.JetViewResolver;
import org.team4u.fhs.web.handler.method.RequestMappingHandlerMapping;
import org.team4u.fhs.web.handler.resource.StaticResourceHandlerMapping;
import org.team4u.fhs.web.handler.simple.FileRuleHttpRouteHandlerMapping;
import org.team4u.fhs.web.handler.simple.SimpleHttpRoute;
import org.team4u.fhs.web.handler.simple.SimpleHttpRouteHandlerMapping;
import org.team4u.kit.core.action.Callback;
import org.team4u.kit.core.action.Callback2;
import org.team4u.kit.core.action.Function;
import retrofit2.Converter;
import retrofit2.Retrofit;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.security.KeyStore;
import java.util.List;
import java.util.Map;

/**
 * @author Jay Wu
 */
public class NettyHttpServerTest {

    @Test
    public void listen() {
        NettyHttpServer httpServer = new NettyHttpServer(new NettyHttpServerConfig()
                .setHttpSessionTimeoutSecs(10));
        httpServer.onRequest(new Callback2<HttpServerRequest, HttpServerResponse>() {
            @Override
            public void invoke(HttpServerRequest request, HttpServerResponse response) {
                for (Map.Entry<String, List<String>> entry : request.getParameterMap().entrySet()) {
                    response.write(entry.getKey() + "=" + entry.getValue() + " ");
                }

                response.write(request.getBody());
                response.write(request.getSession().getId());
            }
        }).listen(7000);

        ThreadUtil.safeSleep(Long.MAX_VALUE);
    }

    @Test
    public void withSimpleHttpRoute() {
        SimpleHttpRouteHandlerMapping mapping = new SimpleHttpRouteHandlerMapping();

        mapping.addRoute(new SimpleHttpRoute()
                .setRequestHandler(new Callback<RoutingContext>() {
                    @Override
                    public void invoke(RoutingContext context) {
                        context.getResponse().write("yes");
                    }
                }).path("/"));

        mapping.addRoute(new SimpleHttpRoute()
                .setRequestHandler(new Callback<RoutingContext>() {
                    @Override
                    public void invoke(RoutingContext context) {
                        context.getResponse().write(context.getRequest().getParameterMap().toString());
                    }
                }).path("/x/y"));

        mapping.addRoute(new SimpleHttpRoute()
                .setRequestHandler(new Function<RoutingContext, ModelAndView>() {
                    @Override
                    public ModelAndView invoke(RoutingContext context) {
                        return new ModelAndView("json", context.getRequest().getParameterMap());
                    }
                }).path("/:id"));

        mapping.addRoute(new SimpleHttpRoute()
                .setRequestHandler(new Callback<RoutingContext>() {
                    @Override
                    public void invoke(RoutingContext context) {
                        throw new RuntimeException("Test");
                    }
                }).path("/error"));

        mapping.addRoute(new SimpleHttpRoute()
                .setRequestHandler(new Function<RoutingContext, ModelAndView>() {
                    @Override
                    public ModelAndView invoke(RoutingContext context) {
                        return new ModelAndView("json", context.getRequest().getParameterMap());
                    }
                }).path("/z/*"));

        startServer(new DefaultHttpRouter().addLastHandlerMapping(mapping), null);
        ThreadUtil.safeSleep(Long.MAX_VALUE);
    }

    @Test
    public void withFileHttpRoute() {
        HandlerMapping mapping = new FileRuleHttpRouteHandlerMapping().addRouteRuleFile("mock/mock.js");
        startServer(new DefaultHttpRouter().addLastHandlerMapping(mapping), null);
        ThreadUtil.safeSleep(Long.MAX_VALUE);
    }

    @Test
    public void withController() throws Exception {
        startServer(new DefaultHttpRouter()
                        .addLastHandlerMapping(new RequestMappingHandlerMapping()
                                .addController(new TestController())
                                .addController(new WebSocketTestController())
                        )
                        .addLastHandlerMapping(new StaticResourceHandlerMapping("webapp/static", "static"))
                        .addLastViewResolver(new JetViewResolver())
                        .addLastViewResolver(new BeetlViewResolver())
                        .addLastViewResolver(new FastJsonViewResolver())
                , null
        );

        ThreadUtil.safeSleep(Long.MAX_VALUE);
    }

    private void startServer(final HttpRouter router, SSLContext sslContext) {
        new NettyHttpServer(new NettyHttpServerConfig()
                .setHttpSessionTimeoutSecs(5)
                .setWebSocketTimeoutSecs(5), sslContext)
                .onRequest(new Callback2<HttpServerRequest, HttpServerResponse>() {
                    @Override
                    public void invoke(HttpServerRequest request, HttpServerResponse response) {
                        router.doRoute(new RoutingContext()
                                .setRequest(request)
                                .setResponse(response));
                    }
                })
                // 设置WebSocket监听器
                .setWebSocketListener(new DefaultWebSocketListener(router))
                .listen(7000);
    }

    @Test
    public void checkTestController() throws IOException {
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Connection", "Keep-Alive")
                        .addHeader("Cookie", "z=z")
                        .build();
                return chain.proceed(newRequest);
            }
        };

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://127.0.0.1:7000")
                .client(client)
                .addConverterFactory(new Converter.Factory() {
                    @Override
                    public Converter<ResponseBody, ?> responseBodyConverter(final Type type,
                                                                            Annotation[] annotations,
                                                                            Retrofit retrofit) {
                        return new Converter<ResponseBody, Object>() {
                            @Override
                            public Object convert(ResponseBody value) throws IOException {
                                if (type == String.class) {
                                    return value.string();
                                }

                                return JSON.parseObject(value.string(), type);
                            }
                        };
                    }

                    @Override
                    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                                          Annotation[] parameterAnnotations,
                                                                          Annotation[] methodAnnotations,
                                                                          Retrofit retrofit) {
                        return new Converter<Object, RequestBody>() {
                            @Override
                            public RequestBody convert(Object value) throws IOException {
                                return RequestBody.create(
                                        MediaType.parse(HttpHeaderName.CONTENT_TYPE.content()),
                                        JSON.toJSONString(value)
                                );
                            }
                        };
                    }

                    @Override
                    public Converter<?, String> stringConverter(Type type,
                                                                Annotation[] annotations,
                                                                Retrofit retrofit) {
                        return new Converter<Object, String>() {
                            @Override
                            public String convert(Object value) {
                                return Convert.toStr(value);
                            }
                        };
                    }
                })
                .build();

        TestApi api = retrofit.create(TestApi.class);
        retrofit2.Response<Dict> jResponse = api.json("x").execute();
        Assert.assertEquals("xyz", jResponse.body().getStr("result"));
        Assert.assertEquals(HttpHeaderValue.APPLICATION_JSON.content() + "; charset=UTF-8",
                jResponse.headers().get(HttpHeaderName.CONTENT_TYPE.content()));

        retrofit2.Response<String> modelResponse = api.model("fjay", "1").execute();
        Assert.assertEquals("fjay1", modelResponse.body());
        Assert.assertEquals(HttpHeaderValue.TEXT_PLAIN.content() + "; charset=UTF-8",
                modelResponse.headers().get(HttpHeaderName.CONTENT_TYPE.content()));

        retrofit2.Response<String> sessionResponse = api.session().execute();
        Assert.assertEquals(32, sessionResponse.body().length());
        Assert.assertTrue(sessionResponse.headers().get(HttpHeaderName.SET_COOKIE.content())
                .contains(HttpServerSession.SESSION_ID_NAME));

        Assert.assertEquals("xy", api.rest("x", "y").execute().body());

        Assert.assertEquals("fjay", api.body(CollUtil.newArrayList(new TestController.User("fjay")))
                .execute().body().get(0).name);

        Assert.assertEquals(HttpStatusCode.BAD_REQUEST.code(), api.error().execute().code());

        Assert.assertEquals("console.log(\"2\");", api.download().execute().body());

        Assert.assertTrue(api.upload(filesToMultipartBody(
                CollUtil.newArrayList(getUploadFile(), getUploadFile())))
                .execute().body().getBool("sameFile"));

        Assert.assertTrue(api.upload(fileToPart("file0", getUploadFile()), fileToPart("file1", getUploadFile()))
                .execute().body().getBool("sameFile"));

        Assert.assertTrue(api.jet("fjay").execute().body().contains("fjay"));

        Assert.assertTrue(api.html().execute().body().contains("Hello"));
    }

    private MultipartBody filesToMultipartBody(List<File> files) {
        MultipartBody.Builder builder = new MultipartBody.Builder();

        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            RequestBody requestBody = RequestBody.create(MediaType.parse(MimeUtil.getMimeType(file)), file);
            builder.addFormDataPart("file" + i, file.getName(), requestBody);
        }

        builder.setType(MultipartBody.FORM);
        return builder.build();
    }

    private RequestBody fileToRequestBody(File file) {
        return RequestBody.create(MediaType.parse(MimeUtil.getMimeType(file)), file);
    }

    private MultipartBody.Part fileToPart(String name, File file) {
        return MultipartBody.Part.createFormData(name, file.getName(), fileToRequestBody(file));
    }

    private File getUploadFile() {
        return FileUtil.file("webapp/static/js/demo.js");
    }

    private SSLContext createSSLContext() throws Exception {
        String password = "1234567";
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(FileUtil.getInputStream("tomcat.jks"), password.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, password.toCharArray());

        SSLContext sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(kmf.getKeyManagers(), null, null);
        return sslcontext;
    }
}