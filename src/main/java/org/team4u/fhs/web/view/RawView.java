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
public class RawView implements View {

    @Override
    public String getContentType() {
        return HttpHeaderValue.TEXT_PLAIN.content();
    }

    @Override
    public void render(Map<String, ?> model, RoutingContext context) throws Exception {
        Object obj = model.get(ModelAndView.SINGLE_MODEL_NAME);
        context.getResponse().write(ValueUtil.defaultIfNull(obj, "").toString());
    }
}