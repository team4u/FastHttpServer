package org.team4u.fhs.web.view;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import org.team4u.fhs.web.View;
import org.team4u.fhs.web.ViewResolver;

import java.util.Locale;

/**
 * @author Jay Wu
 */
public class DefaultViewResolver implements ViewResolver {

    private final static View VOID_VIEW = new VoidView();
    private final static View RAW_VIEW = new RawView();
    private final static View HTML_VIEW = new SimpleHtmlView();

    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        if (StrUtil.equals(viewName, "void")) {
            return VOID_VIEW;
        } else if (StrUtil.equals(viewName, "raw")) {
            return RAW_VIEW;
        } else if (StrUtil.equals(viewName, "html")) {
            return HTML_VIEW;
        } else if (viewName.startsWith("redirect")) {
            String[] viewNameAndPath = viewName.split(":");
            Assert.isTrue(viewNameAndPath.length == 2, "RedirectView must contain path after 'redirect:'");
            String path = viewNameAndPath[1];
            return new RedirectView(path);
        }

        return null;
    }
}