package org.team4u.fhs.web;


/**
 * Workflow interface that allows for customized handler execution chains.
 * Applications can register any number of existing or custom interceptors
 * for certain groups of handlers, to add common preprocessing behavior
 * without needing to modify each handler implementation.
 * <p>
 * <p>A HandlerInterceptor gets called before the appropriate HandlerAdapter
 * triggers the execution of the handler itself. This mechanism can be used
 * for a large field of preprocessing aspects, e.g. for authorization checks,
 * or common handler behavior like locale or theme changes. Its main purpose
 * is to allow for factoring out repetitive handler code.
 * <p>
 * <p>In an async processing scenario, the handler may be executed in a separate
 * thread while the main thread exits without rendering or invoking the
 * {@code postHandle} and {@code afterCompletion} callbacks. When concurrent
 * handler execution completes, the request is dispatched back in order to
 * proceed with rendering the model and all methods of this contract are invoked
 * again. For further options and details see
 * {@code org.springframework.web..AsyncHandlerInterceptor}
 * <p>
 * <p>Typically an interceptor chain is defined per HandlerMapping bean,
 * sharing its granularity. To be able to apply a certain interceptor chain
 * to a group of handlers, one needs to map the desired handlers via one
 * HandlerMapping bean. The interceptors themselves are defined as beans
 * in the application context, referenced by the mapping bean definition
 * via its "interceptors" property (in XML: a &lt;list&gt; of &lt;ref&gt;).
 * <p>
 * <p>HandlerInterceptor is basically similar to a  Filter, but in
 * contrast to the latter it just allows custom pre-processing with the option
 * of prohibiting the execution of the handler itself, and custom post-processing.
 * Filters are more powerful, for example they allow for exchanging the request
 * and response objects that are handed down the chain. Note that a filter
 * gets configured in web.xml, a HandlerInterceptor in the application context.
 * <p>
 * <p>As a basic guideline, fine-grained handler-related preprocessing tasks are
 * candidates for HandlerInterceptor implementations, especially factored-out
 * common handler code and authorization checks. On the other hand, a Filter
 * is well-suited for request content and view content handling, like multipart
 * forms and GZIP compression. This typically shows when one needs to map the
 * filter to certain content types (e.g. images), or to all requests.
 *
 * @author Jay Wu
 */
public interface HandlerInterceptor {

    /**
     * Intercept the execution of a handler. Called after HandlerMapping determined
     * an appropriate handler object, but before HandlerAdapter invokes the handler.
     * <p>
     * <p>Dispatcher processes a handler in an execution chain, consisting
     * of any number of interceptors, with the handler itself at the end.
     * With this method, each interceptor can decide to abort the execution chain,
     * typically sending a HTTP error or writing a custom response.
     * <p>
     * <p><strong>Note:</strong> special considerations apply for asynchronous
     * request processing.
     *
     * @param handler chosen handler to execute, for type and/or instance evaluation
     * @return {@code true} if the execution chain should proceed with the
     * next interceptor or the handler itself. Else, Dispatcher assumes
     * that this interceptor has already dealt with the response itself.
     * @throws Exception in case of errors
     */
    boolean preHandle(RoutingContext context, Object handler)
            throws Exception;

    /**
     * Intercept the execution of a handler. Called after HandlerAdapter actually
     * invoked the handler, but before the Dispatcher renders the view.
     * Can expose additional model objects to the view via the given ModelAndView.
     * <p>
     * <p>Dispatcher processes a handler in an execution chain, consisting
     * of any number of interceptors, with the handler itself at the end.
     * With this method, each interceptor can post-process an execution,
     * getting applied in inverse order of the execution chain.
     * <p>
     * <p><strong>Note:</strong> special considerations apply for asynchronous
     * request processing. For more details see
     *
     * @param context      current http routing context
     * @param handler      handler that started async
     *                     execution, for type and/or instance examination
     * @param modelAndView the {@code ModelAndView} that the handler returned
     *                     (can also be {@code null})
     * @throws Exception in case of errors
     */
    void postHandle(RoutingContext context, Object handler, ModelAndView modelAndView)
            throws Exception;

    /**
     * Callback after completion of request processing, that is, after rendering
     * the view. Will be called on any outcome of handler execution, thus allows
     * for proper resource cleanup.
     * <p>
     * <p>Note: Will only be called if this interceptor's {@code preHandle}
     * method has successfully completed and returned {@code true}!
     * <p>
     * <p>As with the {@code postHandle} method, the method will be invoked on each
     * interceptor in the chain in reverse order, so the first interceptor will be
     * the last to be invoked.
     * <p>
     * <p><strong>Note:</strong> special considerations apply for asynchronous
     * request processing. For more details see
     *
     * @param context current http routing context
     * @param handler handler that started async
     *                execution, for type and/or instance examination
     * @param ex      exception thrown on handler execution, if any
     * @throws Exception in case of errors
     */
    void afterCompletion(RoutingContext context, Object handler, Exception ex)
            throws Exception;

}
