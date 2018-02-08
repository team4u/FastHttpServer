package org.team4u.fhs.web.view;

import org.team4u.fhs.server.HttpHeaderValue;
import org.team4u.fhs.web.RoutingContext;
import org.team4u.fhs.web.View;

import java.util.Map;

/**
 * @author Jay Wu
 */
public class VoidView implements View {

    @Override
    public String getContentType() {
        return HttpHeaderValue.TEXT_PLAIN.content();
    }

    @Override
    public void render(Map<String, ?> model, RoutingContext context) throws Exception {
    }
}