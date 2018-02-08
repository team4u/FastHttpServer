package org.team4u.fhs.web.ext.view;

import com.alibaba.fastjson.JSON;
import org.team4u.fhs.server.HttpHeaderValue;
import org.team4u.fhs.web.ModelAndView;
import org.team4u.fhs.web.RoutingContext;
import org.team4u.fhs.web.View;

import java.util.Map;

/**
 * @author Jay Wu
 */
public class FastJsonView implements View {

    @Override
    public String getContentType() {
        return HttpHeaderValue.APPLICATION_JSON.content();
    }

    @Override
    public void render(Map<String, ?> model, RoutingContext context) throws Exception {
        Object obj = model.get(ModelAndView.SINGLE_MODEL_NAME);
        if (obj == null) {
            obj = model;
        }

        context.getResponse().write(JSON.toJSONString(obj));
    }
}