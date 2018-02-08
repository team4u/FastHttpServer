package org.team4u.fhs.web.view;

import cn.hutool.core.lang.Assert;
import org.team4u.fhs.server.HttpStatusCode;
import org.team4u.fhs.web.ModelAndView;
import org.team4u.fhs.web.RoutingContext;
import org.team4u.fhs.web.View;

import java.util.Map;

/**
 * @author Jay Wu
 */
public class RedirectView implements View {

    private String path;

    public RedirectView(String path) {
        Assert.notNull(path, "Redirect path is null");
        this.path = path;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public void render(Map<String, ?> model, RoutingContext context) throws Exception {
        if (path.contains("${obj}")) {
            Object obj = model.get(ModelAndView.SINGLE_MODEL_NAME);
            Assert.notNull(obj, "Redirect path is null");
            path = path.replace("${obj}", obj.toString());
        }

        context.getResponse().addHeader("Location", path);
        context.getResponse().setStatus(HttpStatusCode.FOUND.code());
    }
}