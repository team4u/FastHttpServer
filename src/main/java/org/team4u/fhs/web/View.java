package org.team4u.fhs.web;


import java.util.Map;

/**
 * MVC View for a web interaction. Implementations are responsible for rendering
 * content, and exposing the model. A single view exposes multiple model attributes.
 * <p>
 * <p>Views should be beans. They are likely to be instantiated as beans by a ViewResolver.
 * As this interface is stateless, view implementations should be thread-safe.
 *
 * @author Jay Wu
 */
public interface View {


    /**
     * Return the content type of the view, if predetermined.
     * <p>Can be used to check the content type upfront,
     * before the actual rendering process.
     *
     * @return the content type String (optionally including a character set),
     * or {@code null} if not predetermined.
     */
    String getContentType();

    /**
     * Render the view given the specified model.
     * <p>The first step will be preparing the request: In the JSP case,
     * this would mean setting model objects as request attributes.
     * The second step will be the actual rendering of the view,
     * for example including the JSP via a RequestDispatcher.
     *
     * @param model Map with name Strings as keys and corresponding model
     *              objects as ValueUtil (Map can also be {@code null} in case of empty model)
     * @throws Exception if rendering failed
     */
    void render(Map<String, ?> model, RoutingContext context) throws Exception;
}