package org.team4u.fhs.web;

import org.team4u.fhs.server.HttpMethod;
import org.team4u.fhs.server.HttpServerRequest;

/**
 * @author Jay Wu
 */
public interface HttpRoute {

    boolean matches(HttpServerRequest request);

    /**
     * Add an HTTP method for this route. By default a route will match all HTTP methods. If any are specified then the route
     * will only match any of the specified methods
     *
     * @param method the HTTP method to add
     * @return a reference to this, so the API can be used fluently
     */
    HttpRoute httpMethod(HttpMethod method);

    /**
     * Set the path prefix for this route. If set then this route will only match request URI paths which start with this
     * path prefix. Only a single path or path regex can be set for a route.
     *
     * @param path the path prefix
     * @return a reference to this, so the API can be used fluently
     */
    HttpRoute path(String path);

    /**
     * Set the path prefix as a regular expression. If set then this route will only match request URI paths, the beginning
     * of which match the regex. Only a single path or path regex can be set for a route.
     *
     * @param path the path regex
     * @return a reference to this, so the API can be used fluently
     */
    HttpRoute pathRegex(String path);

    /**
     * Add a content type produced by this route. Used for content based routing.
     *
     * @param contentType the content type
     * @return a reference to this, so the API can be used fluently
     */
    HttpRoute produces(String contentType);

    /**
     * Add a content type consumed by this route. Used for content based routing.
     *
     * @param contentType the content type
     * @return a reference to this, so the API can be used fluently
     */
    HttpRoute consumes(String contentType);

    /**
     * Disable this route. While disabled the router will not route any requests or failures to it.
     *
     * @return a reference to this, so the API can be used fluently
     */
    HttpRoute disable();

    /**
     * Enable this route.
     *
     * @return a reference to this, so the API can be used fluently
     */
    HttpRoute enable();

    /**
     * @return the path prefix (if any) for this route
     */
    String getPath();
}
