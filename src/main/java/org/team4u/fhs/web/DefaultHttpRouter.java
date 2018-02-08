package org.team4u.fhs.web;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.fhs.server.HttpServerRequest;
import org.team4u.fhs.server.HttpStatusCode;
import org.team4u.kit.core.error.ExceptionUtil;
import org.team4u.kit.core.error.ServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ServiceLoader;

/**
 * @author Jay Wu
 */
public class DefaultHttpRouter implements HttpRouter {

    private final static Log log = LogFactory.get();

    private List<HandlerMapping> handlerMappings = new ArrayList<HandlerMapping>();
    private List<HandlerAdapter> handlerAdapters = new ArrayList<HandlerAdapter>();
    private List<HandlerExceptionResolver> handlerExceptionResolvers = new ArrayList<HandlerExceptionResolver>();
    private List<ViewResolver> viewResolvers = new ArrayList<ViewResolver>();

    public DefaultHttpRouter() {
        initDefaultHandler();
    }

    @Override
    public boolean accept(HttpServerRequest request) {
        HandlerExecutionChain mappedHandler = null;
        try {
            mappedHandler = getHandler(request);
        } catch (Exception e) {
            throw ExceptionUtil.toRuntimeException(e);
        }

        return mappedHandler != null && mappedHandler.getHandler() != null;
    }

    @Override
    public void doRoute(RoutingContext context) {
        HandlerExecutionChain mappedHandler = null;
        Exception routeException = null;

        ModelAndView mv = null;

        try {
            mappedHandler = getHandler(context.getRequest());

            if (mappedHandler == null || mappedHandler.getHandler() == null) {
                noHandlerFound(context);
                return;
            }

            // Determine handler adapter for the current request.
            HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());

            if (!mappedHandler.applyPreHandle(context)) {
                return;
            }

            // Actually invoke the handler.
            mv = ha.handle(context, mappedHandler.getHandler());

            mappedHandler.applyPostHandle(context, mv);
        } catch (Exception e) {
            routeException = e;
        }

        try {
            processRouteResult(context, mappedHandler, mv, routeException);
        } catch (Exception e) {
            try {
                triggerAfterCompletion(context, mappedHandler, e);
            } catch (Exception e1) {
                throw ExceptionUtil.toRuntimeException(e1);
            }
        }
    }

    protected void initDefaultHandler() {
        for (HandlerMapping mapping : ServiceLoader.load(HandlerMapping.class)) {
            addLastHandlerMapping(mapping);
        }

        for (HandlerAdapter adapter : ServiceLoader.load(HandlerAdapter.class)) {
            addLastHandlerAdapter(adapter);
        }

        for (ViewResolver resolver : ServiceLoader.load(ViewResolver.class)) {
            addLastViewResolver(resolver);
        }

        for (HandlerExceptionResolver resolver : ServiceLoader.load(HandlerExceptionResolver.class)) {
            addLastHandlerExceptionResolver(resolver);
        }
    }

    /**
     * Return the HandlerExecutionChain for this request.
     * <p>Tries all handler mappings in order.
     *
     * @param request current HTTP request
     * @return the HandlerExecutionChain, or {@code null} if no handler could be found
     */
    protected HandlerExecutionChain getHandler(HttpServerRequest request) throws Exception {
        for (HandlerMapping hm : this.handlerMappings) {
            if (log.isTraceEnabled()) {
                log.trace(
                        "Testing handler map [" + hm + "] in DefaultHttpRouter with uri '" + request.getRequestURI() + "'");
            }
            HandlerExecutionChain handler = hm.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        return null;
    }

    /**
     * No handler found -> set appropriate HTTP response status.
     */
    protected void noHandlerFound(RoutingContext context) throws Exception {
        context.getResponse().sendError(HttpStatusCode.NOT_FOUND.code());
    }

    /**
     * Return the HandlerAdapter for this handler object.
     *
     * @param handler the handler object to find an adapter for
     */
    protected HandlerAdapter getHandlerAdapter(Object handler) {
        for (HandlerAdapter ha : this.handlerAdapters) {
            if (log.isTraceEnabled()) {
                log.trace("Testing handler adapter [" + ha + "]");
            }
            if (ha.supports(handler)) {
                return ha;
            }
        }
        throw new ServiceException("No adapter for handler [" + handler +
                "]: The DefaultHttpRouter configuration needs to include a HandlerAdapter that supports this handler");
    }

    /**
     * Handle the result of handler selection and handler invocation, which is
     * either a ModelAndView or an Exception to be resolved to a ModelAndView.
     */
    private void processRouteResult(RoutingContext context,
                                    HandlerExecutionChain mappedHandler, ModelAndView mv, Exception exception) throws Exception {

        boolean errorView = false;

        if (exception != null) {
            Object handler = (mappedHandler != null ? mappedHandler.getHandler() : null);
            mv = processHandlerException(context, handler, exception);
            errorView = (mv != null);
        }

        // Did the handler return a view to render?
        if (mv != null && !mv.wasCleared()) {
            render(mv, context);
            if (errorView) {
                context.getRequest().removeAttribute(EXCEPTION_ATTRIBUTE);
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Null ModelAndView returned to DefaultHttpRouter with uri '"
                        + context.getRequest().getRequestURI()
                        + "': assuming HandlerAdapter completed request handling");
            }
        }

        if (mappedHandler != null) {
            mappedHandler.triggerAfterCompletion(context, null);
        }
    }

    /**
     * Determine an error ModelAndView via the registered HandlerExceptionResolvers.
     *
     * @param handler the executed handler, or {@code null} if none chosen at the time of the exception
     *                (for example, if multipart resolution failed)
     * @param ex      the exception that got thrown during handler execution
     * @return a corresponding ModelAndView to forward to
     * @throws Exception if no error ModelAndView found
     */
    protected ModelAndView processHandlerException(RoutingContext context,
                                                   Object handler, Exception ex) throws Exception {
        // Check registered HandlerExceptionResolvers...
        ModelAndView exMv = null;
        for (HandlerExceptionResolver handlerExceptionResolver : this.handlerExceptionResolvers) {
            exMv = handlerExceptionResolver.resolveException(context, handler, ex);
            if (exMv != null) {
                break;
            }
        }
        if (exMv != null) {
            context.getRequest().setAttribute(EXCEPTION_ATTRIBUTE, ex);
            return exMv;
        }

        throw ex;
    }

    /**
     * Render the given ModelAndView.
     * <p>This is the last stage in handling a request. It may involve resolving the view by name.
     *
     * @param mv the ModelAndView to render
     * @throws Exception if there's a problem rendering the view
     */
    protected void render(ModelAndView mv, RoutingContext context) throws Exception {
        View view;
        if (mv.isReference()) {
            // We need to resolve the view name.
            view = resolveViewName(mv.getViewName(), null);
            if (view == null) {
                throw new ServiceException("Could not resolve view with name '" + mv.getViewName() +
                        "' in router with uri '" + context.getRequest().getRequestURI() + "'");
            }
        } else {
            // No need to lookup: the ModelAndView object contains the actual View object.
            view = mv.getView();
            if (view == null) {
                throw new ServiceException("ModelAndView [" + mv + "] neither contains a view name nor a " +
                        "View object in router with uri '" + context.getRequest().getRequestURI() + "'");
            }
        }

        // Delegate to the View object for rendering.
        if (log.isDebugEnabled()) {
            log.debug("Rendering view [" + view + "] in router with uri '" + context.getRequest().getRequestURI() + "'");
        }
        try {
            if (mv.getStatus() != null) {
                context.getResponse().setStatus(mv.getStatus());
            }

            if (StrUtil.isEmpty(context.getResponse().getContentType())) {
                context.getResponse().setContentType(view.getContentType());
            }

            view.render(mv.getModelInternal(), context);
        } catch (Exception ex) {
            if (log.isDebugEnabled()) {
                log.debug("Error rendering view [" + view + "] in router with uri '" +
                        context.getRequest().getRequestURI() + "'", ex);
            }
            throw ex;
        }
    }

    /**
     * Resolve the given view name into a View object (to be rendered).
     * <p>The default implementations asks all ViewResolvers of this router.
     * Can be overridden for custom resolution strategies, potentially based on
     * specific model attributes or request parameters.
     *
     * @param viewName the name of the view to resolve
     * @param locale   the current locale
     * @return the View object, or {@code null} if none found
     * @throws Exception if the view cannot be resolved
     *                   (typically in case of problems creating an actual View object)
     * @see ViewResolver#resolveViewName
     */
    protected View resolveViewName(String viewName, Locale locale) throws Exception {
        for (ViewResolver viewResolver : this.viewResolvers) {
            View view = viewResolver.resolveViewName(viewName, locale);
            if (view != null) {
                return view;
            }
        }
        return null;
    }

    private void triggerAfterCompletion(RoutingContext context,
                                        HandlerExecutionChain mappedHandler, Exception ex) throws Exception {
        if (mappedHandler != null) {
            mappedHandler.triggerAfterCompletion(context, ex);
        }
        throw ex;
    }

    public DefaultHttpRouter addFirstHandlerMapping(HandlerMapping mapping) {
        handlerMappings.add(0, mapping);
        return this;
    }

    public DefaultHttpRouter addLastHandlerMapping(HandlerMapping mapping) {
        handlerMappings.add(mapping);
        return this;
    }

    public DefaultHttpRouter addFirstHandlerAdapter(HandlerAdapter adapter) {
        handlerAdapters.add(0, adapter);
        return this;
    }

    public DefaultHttpRouter addLastHandlerAdapter(HandlerAdapter adapter) {
        handlerAdapters.add(adapter);
        return this;
    }

    public DefaultHttpRouter addFirstHandlerExceptionResolver(HandlerExceptionResolver resolver) {
        handlerExceptionResolvers.add(0, resolver);
        return this;
    }

    public DefaultHttpRouter addLastHandlerExceptionResolver(HandlerExceptionResolver resolver) {
        handlerExceptionResolvers.add(resolver);
        return this;
    }

    public DefaultHttpRouter addFirstViewResolver(ViewResolver resolver) {
        viewResolvers.add(0, resolver);
        return this;
    }

    public DefaultHttpRouter addLastViewResolver(ViewResolver resolver) {
        viewResolvers.add(resolver);
        return this;
    }

    public List<HandlerMapping> getHandlerMappings() {
        return handlerMappings;
    }

    public DefaultHttpRouter setHandlerMappings(List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
        return this;
    }

    public List<HandlerAdapter> getHandlerAdapters() {
        return handlerAdapters;
    }

    public DefaultHttpRouter setHandlerAdapters(List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
        return this;
    }

    public List<HandlerExceptionResolver> getHandlerExceptionResolvers() {
        return handlerExceptionResolvers;
    }

    public DefaultHttpRouter setHandlerExceptionResolvers(List<HandlerExceptionResolver> handlerExceptionResolvers) {
        this.handlerExceptionResolvers = handlerExceptionResolvers;
        return this;
    }

    public List<ViewResolver> getViewResolvers() {
        return viewResolvers;
    }

    public DefaultHttpRouter setViewResolvers(List<ViewResolver> viewResolvers) {
        this.viewResolvers = viewResolvers;
        return this;
    }
}