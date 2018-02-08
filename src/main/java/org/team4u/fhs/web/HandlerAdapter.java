package org.team4u.fhs.web;

/**
 * MVC framework SPI, allowing parameterization of the core MVC workflow.
 * <p>
 * <p>Interface that must be implemented for each handler type to handle a request.
 * This interface is used to allow the {@link DefaultHttpRouter} to be indefinitely
 * extensible. The {@code DispatcherServlet} accesses all installed handlers through
 * this interface, meaning that it does not contain code specific to any handler type.
 * <p>
 * <p>Note that a handler can be of type {@code Object}. This is to enable
 * handlers from other frameworks to be integrated with this framework without
 * custom coding, as well as to allow for annotation-driven handler objects that
 * do not obey any specific Java interface.
 * <p>
 * <p>This interface is not intended for application developers. It is available
 * to handlers who want to develop their own web workflow.
 * <p>
 *
 * @author Jay Wu
 */
public interface HandlerAdapter {

    /**
     * Given a handler instance, return whether or not this {@code HandlerAdapter}
     * can support it. Typical HandlerAdapters will base the decision on the handler
     * type. HandlerAdapters will usually only support one handler type each.
     * <p>A typical implementation:
     * <p>{@code
     * return (handler instanceof MyHandler);
     * }
     *
     * @param handler handler object to check
     * @return whether or not this object can use the given handler
     */
    boolean supports(Object handler);

    /**
     * Use the given handler to handle this request.
     * The workflow that is required may vary widely.
     *
     * @param handler handler to use. This object must have previously been passed
     *                to the {@code supports} method of this interface, which must have
     *                returned {@code true}.
     * @return ModelAndView object with the name of the view and the required
     * model data, or {@code null} if the request has been handled directly
     * @throws Exception in case of errors
     */
    ModelAndView handle(RoutingContext context, Object handler) throws Exception;
}