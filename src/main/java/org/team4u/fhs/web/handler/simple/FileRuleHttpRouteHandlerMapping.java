package org.team4u.fhs.web.handler.simple;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSON;
import org.team4u.fhs.server.HttpMethod;
import org.team4u.fhs.web.ModelAndView;
import org.team4u.fhs.web.RoutingContext;
import org.team4u.kit.core.action.Function;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jay Wu
 */
public class FileRuleHttpRouteHandlerMapping extends SimpleHttpRouteHandlerMapping {

    private static final Log log = LogFactory.get();

    public FileRuleHttpRouteHandlerMapping addRouteRuleFile(String ruleFilePath) {
        return addRouteRule(FileUtil.readUtf8String(ruleFilePath));
    }

    public FileRuleHttpRouteHandlerMapping addRouteRule(String ruleContent) {
        Rule rule = JSON.parseObject(ruleContent, Rule.class);

        for (final Rule.Action action : rule.actions) {
            SimpleHttpRoute route = new SimpleHttpRoute(action.request.path);
            if (StrUtil.isNotBlank(action.request.method)) {
                route.httpMethod(HttpMethod.valueOf(action.request.method));
            }

            addRoute(route.setRequestHandler(new Function<RoutingContext, ModelAndView>() {
                @Override
                public ModelAndView invoke(RoutingContext context) {
                    for (Map.Entry<String, Object> entry : action.response.header.entrySet()) {
                        context.getResponse().setHeader(entry.getKey(), entry.getValue());
                    }

                    if (StrUtil.isNotBlank(action.response.view)) {
                        return new ModelAndView(action.response.view, action.response.body);
                    }

                    if (StrUtil.isNotBlank(action.response.file)) {
                        context.getResponse().write(FileUtil.file(action.response.file));
                        return null;
                    }

                    context.getResponse().write(JSON.toJSONString(action.response.body));
                    return null;
                }
            }));
        }

        return this;
    }


    public static class Rule {

        public List<Action> actions = new ArrayList<Action>();

        public static class Action {

            public Request request;

            public Response response;

            public static class Request {

                public String path;

                public String method;

                public boolean enable = true;
            }

            public static class Response {

                public Dict header = new Dict();

                public String file;

                public String view;

                public Object body;
            }
        }

    }

}
