package org.team4u.fhs.web.handler.resource;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.fhs.server.HttpHeaderName;
import org.team4u.fhs.server.HttpServerRequest;
import org.team4u.fhs.server.HttpStatusCode;
import org.team4u.fhs.server.util.GmtTimeUtil;
import org.team4u.fhs.server.util.HttpUtil;
import org.team4u.fhs.server.util.MimeUtil;
import org.team4u.fhs.web.HandlerAdapter;
import org.team4u.fhs.web.ModelAndView;
import org.team4u.fhs.web.RoutingContext;

import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Jay Wu
 */
public class StaticResourceHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof StaticResource;
    }

    @Override
    public ModelAndView handle(RoutingContext context, Object handler) throws Exception {
        StaticResource resource = (StaticResource) handler;

        ModelAndView mv = new ModelAndView("void");

        if (!isModified(context.getRequest(), resource.lastModified())) {
            mv.setStatus(HttpStatusCode.NOT_MODIFIED.code());
            context.getResponse().setHeader(HttpHeaderName.DATE.content(),
                    GmtTimeUtil.FORMATTER.format(new GregorianCalendar().getTime()));
        } else {
            HttpUtil.setLastModifiedAndCache(
                    context.getResponse(),
                    new Date(resource.lastModified()),
                    HttpUtil.DEFAULT_CACHE_SECOND
            );

            context.getResponse().setContentType(MimeUtil.getMimeType(FileUtil.extName(resource.getPath())));
            context.getResponse().write(resource.getContent());
        }

        return mv;
    }

    protected boolean isModified(HttpServerRequest request, long fileLastModified) {
        String ifModifiedSince = request.getHeader(HttpHeaderName.IF_MODIFIED_SINCE.content());
        if (StrUtil.isEmpty(ifModifiedSince)) {
            return true;
        }

        try {
            Date ifModifiedSinceDate = GmtTimeUtil.FORMATTER.parse(ifModifiedSince);
            return ifModifiedSinceDate.getTime() / 1000 != fileLastModified / 1000;
        } catch (ParseException e) {
            // Ignore error
        }

        return true;
    }
}