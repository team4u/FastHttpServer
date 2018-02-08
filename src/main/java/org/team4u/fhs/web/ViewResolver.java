package org.team4u.fhs.web;

import java.util.Locale;

/**
 * Interface to be implemented by objects that can resolve views by name.
 * <p>
 * <p>View state doesn't change during the running of the application,
 * so implementations are free to cache views.
 * <p>
 * <p>Implementations are encouraged to support internationalization,
 * i.e. localized view resolution.
 *
 * @author Jay Wu
 */
public interface ViewResolver {

    /**
     * Resolve the given view by name.
     * <p>Note: To allow for ViewResolver chaining, a ViewResolver should
     * return {@code null} if a view with the given name is not defined in it.
     * However, this is not required: Some ViewResolvers will always attempt
     * to build View objects with the given name, unable to return {@code null}
     * (rather throwing an exception when View creation failed).
     *
     * @param viewName name of the view to resolve
     * @param locale   Locale in which to resolve the view.
     *                 ViewResolvers that support internationalization should respect this.
     * @return the View object, or {@code null} if not found
     * (optional, to allow for ViewResolver chaining)
     * @throws Exception if the view cannot be resolved
     *                   (typically in case of problems creating an actual View object)
     */
    View resolveViewName(String viewName, Locale locale) throws Exception;

}
