package org.team4u.fhs.web;

/**
 * Interface to be implemented by objects that can resolve exceptions thrown during
 * handler mapping or execution, in the typical case to error views. Implementors are
 * typically registered as beans in the application context.
 * <p>
 * <p>Error views are analogous to JSP error pages but can be used with any kind of
 * exception including any checked exception, with potentially fine-grained mappings for
 * specific handlers.
 *
 * @author Jay Wu
 */
public interface HandlerExceptionResolver {

    /**
     * Try to resolve the given exception that got thrown during handler execution,
     * returning a {@link ModelAndView} that represents a specific error page if appropriate.
     * <p>The returned {@code ModelAndView} may be {@linkplain ModelAndView#isEmpty() empty}
     * to indicate that the exception has been resolved successfully but that no view
     * should be rendered, for instance by setting a status code.
     *
     * @param handler the executed handler, or {@code null} if none chosen at the
     *                time of the exception (for example, if multipart resolution failed)
     * @param ex      the exception that got thrown during handler execution
     * @return a corresponding {@code ModelAndView} to forward to, or {@code null}
     * for default processing
     */
    ModelAndView resolveException(RoutingContext context, Object handler, Exception ex);

}