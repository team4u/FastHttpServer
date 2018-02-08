
# FastHttpServer
FastHttpServer为基于Netty轻量级/高性能的HttpServer，无需依赖J2EE容器独立运行。

FastHttpServer有以下几个特点：

1. 支持REST风格参数
2. 支持复杂路径路由
3. 支持文件上传下载
4. 支持静态资源渲染
5. 支持WebSocket
6. 内置 Json/jetbrick-template/beetl 三种视图且可扩展
7. 支持自定义拦截器
8. 支持嵌入式运行


## HOW TO USE
### 服务端
#### 添加包依赖(Maven:pom.xml)
```xml
<dependency>
  <groupId>org.team4u.fhs</groupId>
  <artifactId>fast-http-server</artifactId>
  <version>1.0.0</version>
</dependency>
<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-all</artifactId>
    <version>4.0.3</version>
</dependency>
<!--可选依赖包-->
<!--For JetView-->
<dependency>
  <groupId>com.github.subchen</groupId>
  <artifactId>jetbrick-template</artifactId>
  <version>2.1.2</version>
</dependency>
<!--For JsonView-->
<dependency>
  <groupId>com.alibaba</groupId>
  <artifactId>fastjson</artifactId>
  <version>1.2.13</version>
</dependency>
```

加入仓库：

```xml
<repositories>
    <repository>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
        <id>bintray-team4u</id>
        <name>bintray</name>
        <url>https://dl.bintray.com/team4u/team4u</url>
    </repository>
</repositories>
```
#### Quick Start

创建标准Controller

```java
@Controller("test")
public class TestController {
    /**
     * 允许所有方法请求,指定访问路径为j
     */
    @RequestMapping("/j")
    @ResponseView(success = "json")
    public Object json(HttpServerRequest request,
                       @RequestHeader("y") String y,
                       @CookieValue("z") String z) {
        return new SimpleMap("result", request.getParameter("x") + y + z);
    }

    /**
     * 支持简单参数映射为对象
     * <p>
     * name=fjay
     * numbers[0]=1
     * <p>
     * 对象[list索引] = 值
     * 对象[list索引].属性 = 值
     * 对象.list索引 = 值
     * 对象.list索引.属性 = 值
     * 对象.list:索引 = 值
     * 对象.list:索引.属性 = 值
     * 对象.map(key) = 值
     * 对象.map(key).属性 = 值
     * 对象.map.key = 值
     * 对象.map.key.属性 = 值
     */
    @RequestMapping
    @ResponseView(success = "raw")
    public String model(@RequestParam("..") User user) {
        return user.name + user.numbers.get(0);
    }

    @RequestMapping("/session/*")
    @ResponseView(success = "raw")
    public Object session(RoutingContext context) {
        return context.getRequest().getSession().getId();
    }

    /**
     * 支持路径参数,以:开头
     */
    @RequestMapping("/rest/:x/:y")
    public void rest(HttpServerResponse response,
                     @RequestParam("x") String x,
                     @RequestParam("y") String y) {
        response.write(x + y);
    }

    /**
     * 获取POST流信息
     */
    @RequestMapping(method = HttpMethod.POST)
    @ResponseView(success = "json")
    public List<User> body(@RequestBody(RequestBodyFastJsonDecoder.class) List<User> list) {
        return list;
    }

    /**
     * 测试异常
     */
    @RequestMapping
    public void error() {
        throw new ServiceException("test");
    }

    /**
     * 下载文件
     */
    @RequestMapping
    public void download(HttpServerResponse response) {
        response.write(FileUtil.findFile("webapp/static/js/demo.js"));
    }

    /**
     * 上传文件
     */
    @RequestMapping
    @ResponseView(success = "json")
    public SimpleMap upload(@RequestParam("file0") UploadFile file0,
                            @RequestParam("file1") UploadFile file1) {
        SimpleMap result = new SimpleMap().set("fileName", file0.getFileName())
                .set("path", file0.getFile().getAbsolutePath())
                .set("length", file0.getFile().length())
                .set("sameFile", file0.getFile().length() == file1.getFile().length());

        file0.delete();
        file1.delete();
        return result;
    }

    @RequestMapping
    @ResponseView(success = "jet:index.html")
    public SimpleMap jet(@RequestParam("name") String name) {
        return new SimpleMap("name", Values.defaultIfNull(name, "all"));
    }
    
    /**
     * 重定向
     * <p>
     * 支持以下格式:
     * redirect:${obj}
     * redirect:/test/beetl
     */
    @RequestMapping
    @ResponseView(success = "redirect:${obj}")
    public String redirect() {
        return "/test/beetl";
    }

    public static class User {

        public String name;

        public List<Integer> numbers;

        public User() {
        }

        public User(String name) {
            this.name = name;
        }
    }
}
```

创建WebSocketController

```java
@Controller("webSocketTest")
@ResponseView(success = "void")
public class WebSocketTestController {

    /**
     * 已连接客户端列表
     */
    private List<WebSocketSession> sessions = new ArrayList<WebSocketSession>();

    /**
     * 当WebSocket创建连接时调用此方法,访问路径固定为/onWebSocketOpen
     */
    @RequestMapping
    public void onWebSocketOpen(WebSocketSession session) {
        // 空闲3秒自动断开
        session.setIdleTimeout(3000);
        sessions.add(session);
    }

    /**
     * 当WebSocket关闭连接时调用此方法,访问路径固定为/onWebSocketClose
     */
    @RequestMapping
    public void onWebSocketClose(WebSocketSession session) {
        sessions.remove(session);
    }

    /**
     * 当WebSocket收到文本消息时调用此方法,访问路径固定为/onWebSocketText
     */
    @RequestMapping
    public void onWebSocketText(@RequestParam("id") String id,
                                @RequestBody(RequestBodyFastJsonDecoder.class) TestController.User user) {
        // 为所有客户端发送消息
        for (WebSocketSession s : sessions) {
            s.write(user.name + " from " + id);
        }
    }

    /**
     * 当WebSocket收到二进制消息时调用此方法,访问路径固定为/onWebSocketBinary
     */
    @RequestMapping
    @ResponseView(success = "json")
    public SimpleMap onWebSocketBinary(@RequestBody byte[] body) {
        // 为当前客户端回复消息
        return new SimpleMap("length", body.length);
    }
}
```

WebSocket测试页面

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Test</title>
    <script src="/static/js/demo.js"></script>

    <script type="text/javascript">
        if (!window.WebSocket) {
            window.WebSocket = window.MozWebSocket;
        }

        function createSocket(id) {
            if (window.WebSocket) {
                var socket = new WebSocket("ws://127.0.0.1:7000/webSocketTest?id=" + id);
                socket.onmessage = function (event) {
                    console.log(id + ":" + event.data);
                };
                socket.onopen = function (event) {
                    console.log("open");
                };
                socket.onclose = function (event) {
                    console.log("close");
                };

                return socket;
            } else {
                alert("你的浏览器不支持 WebSocket！");
                return null;
            }
        }

        var s1 = createSocket(1);
        var s2 = createSocket(2);
        s2.binaryType = "arraybuffer";

        var index = 0;
        var timer = setInterval(function () {
            if (index++ > 6) {
                clearInterval(timer);
                return;
            }

            if (index % 2 == 0) {
                var buffer = new ArrayBuffer(1);
                var bytes = new Uint8Array(buffer);
                for (var i = 0; i < bytes.length; i++) {
                    bytes[i] = i;
                }
                s2.send(buffer);
            } else {
                s1.send("{name:'" + new Date().getSeconds() + "'}");
            }
        }, 1000);

    </script>
</head>
<body>
Hello
</body>
</html>
```

启动服务

```java
// 设置控制器注解映射
HandlerMapping controllerMapping = new RequestMappingHandlerMapping()
                            // 通过手工注册控制器
                            .addController(new TestController())
  						  .addController(new WebSocketTestController())
                            // 通过指定包路径自动注册控制器
                            .addControllerPackage("xx.xx")
                            // 设置拦截器
                            .addLastAdaptedInterceptor(interceptor);

// 设置静态资源映射
// 默认为采用ClasspathLoader,可以用FileResource访问外部文件
// 设置classes下的webapp/static/文件夹为静态资源根目录，对外访问路径映射为static
// 例如：webapp/static/文件夹下存在index.html文件，则外部访问路径为http://${host}/static/index.html
// 根目录支持相对路径和绝对路径
HandlerMapping resourceMapping = new StaticResourceHandlerMapping("webapp/static", "static"));

// 设置HTTP路由器
HttpRouter router = new DefaultHttpRouter()
  					// 注册映射器
  					.addLastHandlerMapping(controllerMapping)
					.addLastHandlerMapping(resourceMapping)
  					// 注册jetbrick-template视图处理器
  					.addLastViewResolver(new JetViewResolver())
  					// 注册Beetl视图处理器
                    .addLastViewResolver(new BeetlViewResolver())
					// 注册fastjson视图处理器
	  				.addLastViewResolver(new FastJsonViewResolver())
  					// 注册自定义异常处理器
					.addLastHandlerExceptionResolver(exceptionResolver);
// 设置HTTP服务器
HttpServer server = new NettyHttpServer(
        new NettyHttpServerConfig()
            //设置session最大有效活动时间为10s，不设置，则默认不开启session
            .setHttpSessionTimeoutSecs(10))
        .onRequest(new Callback2<HttpServerRequest, HttpServerResponse>() {
            @Override
            public void invoke(HttpServerRequest request, HttpServerResponse response) {
                // 处理路由转发
                router.doRoute(new RoutingContext()
                        .setRequest(request)
                        .setResponse(response));
            }
        })
 		// 设置WebSocket监听器
        .setWebSocketListener(new DefaultWebSocketListener(router))
        // 指定端口启动HTTP服务
        .listen(9080);
```

### 客户端

直接用浏览器访问以下地址:

```http
http://127.0.0.1:9080/test/j?x=1
http://127.0.0.1:9080/test/model?name=fjay
http://127.0.0.1:9080/test/session
http://127.0.0.1:9080/test/rest/1/2
http://127.0.0.1:9080/test/download
http://127.0.0.1:9080/static/index.html
...
```

END