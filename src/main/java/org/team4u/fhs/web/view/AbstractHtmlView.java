package org.team4u.fhs.web.view;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Dict;
import org.team4u.fhs.server.HttpHeaderValue;
import org.team4u.fhs.web.ModelAndView;
import org.team4u.fhs.web.RoutingContext;
import org.team4u.fhs.web.View;

import java.util.Map;

/**
 * @author Jay Wu
 */
public abstract class AbstractHtmlView implements View {

    protected String templateName;

    public AbstractHtmlView(String templateName) {
        this.templateName = templateName;
    }

    @Override
    public String getContentType() {
        return HttpHeaderValue.TEXT_HTML.content();
    }

    @Override
    public void render(Map<String, ?> model, RoutingContext context) throws Exception {
        //noinspection unchecked
        Map<String, Object> data = (Map<String, Object>) model;
        if (data == null) {
            data = new Dict();
        }

        data.put("request", context.getRequest());

        for (String name : context.getRequest().getAttributeNames()) {
            data.put(name, context.getRequest().getAttribute(name));
        }

        if (templateName.contains("${obj}")) {
            Object obj = model.get(ModelAndView.SINGLE_MODEL_NAME);
            Assert.notNull(obj, "templateName is null");
            templateName = templateName.replace("${obj}", obj.toString());
        }

        context.getResponse().write(renderInternal(model, context));
    }

    protected abstract String renderInternal(Map<String, ?> model, RoutingContext context) throws Exception;
}