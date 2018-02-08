package org.team4u.fhs.web.ext.view;

import jetbrick.template.JetEngine;
import org.team4u.fhs.web.RoutingContext;
import org.team4u.fhs.web.view.AbstractHtmlView;

import java.io.StringWriter;
import java.util.Map;

/**
 * @author Jay Wu
 */
public class JetView extends AbstractHtmlView {

    private static JetEngine engine = JetEngine.create();

    public JetView(String templateName) {
        super(templateName);
    }

    @Override
    public String renderInternal(Map<String, ?> model, RoutingContext context) throws Exception {
        StringWriter sw = new StringWriter();
        //noinspection unchecked
        engine.getTemplate(templateName).render((Map<String, Object>) model, sw);
        return sw.toString();
    }
}