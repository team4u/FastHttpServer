package org.team4u.fhs.web.ext.view;

import org.team4u.fhs.web.View;
import org.team4u.fhs.web.ViewResolver;

import java.util.Locale;

/**
 * @author Jay Wu
 */
public class FastJsonViewResolver implements ViewResolver {

    private final static FastJsonView FAST_JSON_VIEW = new FastJsonView();

    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        if (viewName.equals("json")) {
            return FAST_JSON_VIEW;
        }

        return null;
    }
}