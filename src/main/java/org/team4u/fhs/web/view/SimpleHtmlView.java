package org.team4u.fhs.web.view;

import org.team4u.fhs.server.HttpHeaderValue;
import org.team4u.fhs.web.ModelAndView;
import org.team4u.fhs.web.RoutingContext;
import org.team4u.fhs.web.View;
import org.team4u.kit.core.util.ValueUtil;

import java.util.Map;

/**
 * @author Jay Wu
 */
public class SimpleHtmlView implements View {

    @Override
    public String getContentType() {
        return HttpHeaderValue.TEXT_HTML.content();
    }

    @Override
    public void render(Map<String, ?> model, RoutingContext context) throws Exception {
        Object obj = model.get(ModelAndView.SINGLE_MODEL_NAME);
        if (obj == null) {
            obj = model;
        }

        context.getResponse().write(ValueUtil.defaultIfNull(obj, "").toString());
    }
}