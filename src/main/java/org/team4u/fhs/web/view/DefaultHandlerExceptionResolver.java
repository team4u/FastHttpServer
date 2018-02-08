package org.team4u.fhs.web.view;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.fhs.server.HttpHeaderValue;
import org.team4u.fhs.server.HttpStatusCode;
import org.team4u.fhs.web.HandlerExceptionResolver;
import org.team4u.fhs.web.ModelAndView;
import org.team4u.fhs.web.RoutingContext;
import org.team4u.kit.core.error.ServiceException;
import org.team4u.kit.core.util.ValueUtil;

/**
 * @author Jay Wu
 */
public class DefaultHandlerExceptionResolver implements HandlerExceptionResolver {

    private static final Log log = LogFactory.get();

    @Override
    public ModelAndView resolveException(RoutingContext context, Object handler, Exception ex) {
        String uri = context.getRequest().getRequestURI();

        ModelAndView mv = new ModelAndView("void");
        if (ex instanceof ServiceException) {
            ServiceException se = (ServiceException) ex;
            log.info("Action invoked fail(uri={},code={},message={})", uri, se.getCode(), se.getMessage());
            context.getResponse().setContentType(HttpHeaderValue.APPLICATION_JSON.content());
            context.getResponse().write(String.format("{\"errorCode\" : \"{}\", \"errorMessage\" : \"{}\"}",
                    ValueUtil.defaultIfNull(se.getCode(), ""), ValueUtil.defaultIfNull(se.getMessage(), "")
            ));

            return mv.setStatus(HttpStatusCode.BAD_REQUEST.code());
        } else {
            log.error(String.format("Action invoked fail(uri={}) :", uri), ex);
            return mv.setStatus(HttpStatusCode.INTERNAL_SERVER_ERROR.code());
        }
    }
}