package org.team4u.fhs.web.ext.view;

import cn.hutool.core.lang.Assert;
import org.team4u.fhs.web.View;
import org.team4u.fhs.web.ViewResolver;

import java.util.Locale;

/**
 * @author Jay Wu
 */
public class BeetlViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        if (viewName.startsWith("beetl:")) {
            String[] viewNameAndPath = viewName.split(":");
            Assert.isTrue(viewNameAndPath.length == 2, "JetView must contain template path after 'jet:'");
            String templatePath = viewNameAndPath[1];
            return new BeetlView(templatePath);
        }

        return null;
    }
}