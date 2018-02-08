package org.team4u.fhs.web.ext.view;

import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.team4u.fhs.web.RoutingContext;
import org.team4u.fhs.web.view.AbstractHtmlView;

import java.util.Map;

/**
 * @author Jay Wu
 */
public class BeetlView extends AbstractHtmlView {

    private static GroupTemplate groupTemplate = new GroupTemplate();

    public BeetlView(String templateName) {
        super(templateName);
    }

    @Override
    public String renderInternal(Map<String, ?> model, RoutingContext context) throws Exception {
        Template template = groupTemplate.getTemplate(templateName);
        template.binding(model);

        return template.render();
    }
}