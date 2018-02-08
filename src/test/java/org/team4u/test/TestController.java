package org.team4u.test;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import org.team4u.fhs.server.HttpMethod;
import org.team4u.fhs.server.HttpServerRequest;
import org.team4u.fhs.server.HttpServerResponse;
import org.team4u.fhs.server.UploadFile;
import org.team4u.fhs.web.RoutingContext;
import org.team4u.fhs.web.ext.handler.method.support.RequestBodyFastJsonDecoder;
import org.team4u.fhs.web.handler.method.annotation.*;
import org.team4u.kit.core.error.ServiceException;
import org.team4u.kit.core.util.ValueUtil;

import java.util.List;

/**
 * @author Jay Wu
 */
@Controller("/test")
public class TestController {
    /**
     * 允许所有方法请求,指定访问路径为j
     */
    @RequestMapping("/j")
    @ResponseView(success = "json")
    public Object json(HttpServerRequest request,
                       @RequestHeader("y") String y,
                       @CookieValue("z") String z) {
        return new Dict().set("result", request.getParameter("x") + y + z);
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
        response.write(FileUtil.file("webapp/static/js/demo.js"));
    }

    /**
     * 上传文件
     */
    @RequestMapping
    @ResponseView(success = "json")
    public Dict upload(@RequestParam("file0") UploadFile file0,
                       @RequestParam("file1") UploadFile file1) {
        Dict result = new Dict().set("fileName", file0.getFileName())
                .set("path", file0.getFile().getAbsolutePath())
                .set("length", file0.getFile().length())
                .set("sameFile", file0.getFile().length() == file1.getFile().length());

        file0.delete();
        file1.delete();
        return result;
    }

    @RequestMapping
    @ResponseView(success = "jet:/index.html")
    public void jet(HttpServerRequest request, @RequestParam("name") String name) {
        request.setAttribute("name", ValueUtil.defaultIfNull(name, "all"));
    }

    @RequestMapping
    @ResponseView(success = "beetl:/index.html")
    public Dict beetl(@RequestParam("name") String name) {
        return new Dict()
                .set("name", ValueUtil.defaultIfNull(name, "all"))
                .set("list", CollUtil.newArrayList(1, 2, 3));
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