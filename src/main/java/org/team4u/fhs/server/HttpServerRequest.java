package org.team4u.fhs.server;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Jay Wu
 */
public interface HttpServerRequest {

    /**
     * Returns the value of the named attribute as an <code>Object</code>,
     * or <code>null</code> if no attribute of the given name exists.
     *
     * @param name a <code>String</code> specifying the name of the attribute
     * @return an <code>Object</code> containing the value of the attribute,
     * or <code>null</code> if the attribute does not exist
     */
    Object getAttribute(String name);

    /**
     * Returns an <code>Enumeration</code> containing the
     * names of the attributes available to this request.
     * This method returns an empty <code>Enumeration</code>
     * if the request has no attributes available to it.
     *
     * @return an <code>Enumeration</code> of strings containing the names
     * of the request's attributes
     */
    Collection<String> getAttributeNames();

    /**
     * Stores an attribute in this request.
     * <p>
     * <p>Attribute names should follow the same conventions as
     * package names. Names beginning with <code>java.*</code>,
     * <code>javax.*</code>, and <code>com.sun.*</code>, are
     * reserved for use by Sun Microsystems.
     * <br> If the object passed in is null, the effect is the same as
     * calling {@link #removeAttribute}.
     * <br> It is warned that when the request is dispatched from the
     * servlet resides in a different web application by
     * <code>RequestDispatcher</code>, the object set by this method
     * may not be correctly retrieved in the caller servlet.
     *
     * @param name a <code>String</code> specifying
     *             the name of the attribute
     * @param o    the <code>Object</code> to be stored
     */
    HttpServerRequest setAttribute(String name, Object o);

    /**
     * Removes an attribute from this request.  This method is not
     * generally needed as attributes only persist as long as the request
     * is being handled.
     * <p>
     * <p>Attribute names should follow the same conventions as
     * package names. Names beginning with <code>java.*</code>,
     * <code>javax.*</code>, and <code>com.sun.*</code>, are
     * reserved for use by Sun Microsystems.
     *
     * @param name a <code>String</code> specifying
     *             the name of the attribute to remove
     */
    HttpServerRequest removeAttribute(String name);

    /**
     * Returns the length, in bytes, of the request body and made available by
     * the input stream, or -1 if the length is not known ir is greater than
     * Integer.MAX_VALUE.
     *
     * @return an integer containing the length of the request body or -1 if
     * the length is not known or is greater than Integer.MAX_VALUE.
     */
    int getContentLength();

    /**
     * Returns the MIME type of the body of the request, or
     * <code>null</code> if the type is not known.
     *
     * @return a <code>String</code> containing the name
     * of the MIME type of
     * the request, or null if the type is not known
     */

    public String getContentType();

    /**
     * Returns the value of a request parameter as a <code>String</code>,
     * or <code>null</code> if the parameter does not exist. Request parameters
     * are extra information sent with the request.
     * <p>
     * <p>You should only use this method when you are sure the
     * parameter has only one value. If the parameter might have
     * more than one value, use {@link #getParameterValueUtil}.
     *
     * @param name a <code>String</code> specifying the name of the parameter
     * @return a <code>String</code> representing the single value of
     * the parameter
     * @see #getParameterValueUtil
     */
    String getParameter(String name);

    /**
     * Returns an <code>Enumeration</code> of <code>String</code>
     * objects containing the names of the parameters contained
     * in this request. If the request has
     * no parameters, the method returns an empty <code>Enumeration</code>.
     *
     * @return an <code>Enumeration</code> of <code>String</code>
     * objects, each <code>String</code> containing the name of
     * a request parameter; or an empty <code>Enumeration</code>
     * if the request has no parameters
     */
    Set<String> getParameterNames();

    /**
     * Returns an array of <code>String</code> objects containing
     * all of the ValueUtil the given request parameter has, or
     * <code>null</code> if the parameter does not exist.
     * <p>
     * <p>If the parameter has a single value, the array has a length
     * of 1.
     *
     * @param name a <code>String</code> containing the name of
     *             the parameter whose value is requested
     * @return an array of <code>String</code> objects
     * containing the parameter's ValueUtil
     * @see #getParameter
     */
    List<String> getParameterValueUtil(String name);

    /**
     * Returns a java.util.Map of the parameters of this request.
     * <p>
     *
     * @return an immutable java.util.Map containing parameter names as
     * keys and parameter ValueUtil as map ValueUtil. The keys in the parameter
     * map are of type String. The ValueUtil in the parameter map are of type
     * String List.
     */
    Map<String, List<String>> getParameterMap();

    byte[] getBody();

    String getBodyString();

    /**
     * Returns the name and version of the protocol the request uses
     * in the form <i>protocol/majorVersion.minorVersion</i>, for
     * example, HTTP/1.1.
     *
     * @return a <code>String</code> containing the protocol
     * name and version number
     */
    String getProtocol();

    /**
     * Returns the name of the scheme used to make this request,
     * for example,
     * <code>http</code>, <code>https</code>, or <code>ftp</code>.
     * Different schemes have different rules for constructing URLs,
     * as noted in RFC 1738.
     *
     * @return a <code>String</code> containing the name
     * of the scheme used to make this request
     */
    String getScheme();

    /**
     * Get the address of the local side.
     *
     * @return the local side address
     */
    InetSocketAddress getLocalAddress();

    /**
     * Get the address of the remote side.
     *
     * @return the remote side address
     */
    InetSocketAddress getRemoteAddress();

    /**
     * Returns an array containing all of the <code>Cookie</code>
     * objects the client sent with this request.
     * This method returns <code>null</code> if no cookies were sent.
     *
     * @return an array of all the <code>Cookies</code>
     * included with this request, or <code>null</code>
     * if the request has no cookies
     */
    Collection<Cookie> getCookies();

    Cookie getCookie(String name);

    /**
     * Returns the value of the specified request header
     * as a <code>String</code>. If the request did not include a header
     * of the specified name, this method returns <code>null</code>.
     * If there are multiple headers with the same name, this method
     * returns the first head in the request.
     * The header name is case insensitive. You can use
     * this method with any request header.
     *
     * @param name a <code>String</code> specifying the
     *             header name
     * @return a <code>String</code> containing the
     * value of the requested
     * header, or <code>null</code>
     * if the request does not
     * have a header of that name
     */
    String getHeader(String name);

    /**
     * Returns all the ValueUtil of the specified request header
     * as an <code>Enumeration</code> of <code>String</code> objects.
     * <p>
     * <p>Some headers, such as <code>Accept-Language</code> can be sent
     * by clients as several headers each with a different value rather than
     * sending the header as a comma separated list.
     * <p>
     * <p>If the request did not include any headers
     * of the specified name, this method returns an empty
     * <code>Enumeration</code>.
     * The header name is case insensitive. You can use
     * this method with any request header.
     *
     * @param name a <code>String</code> specifying the
     *             header name
     * @return an <code>Enumeration</code> containing
     * the ValueUtil of the requested header. If
     * the request does not have any headers of
     * that name return an empty
     * enumeration. If
     * the container does not allow access to
     * header information, return null
     */
    Collection<String> getHeaders(String name);

    /**
     * Returns an enumeration of all the header names
     * this request contains. If the request has no
     * headers, this method returns an empty enumeration.
     * <p>
     *
     * @return an enumeration of all the
     * header names sent with this
     * request; if the request has
     * no headers, an empty enumeration;
     */
    Set<String> getHeaderNames();

    /**
     * Returns the value of the specified request header
     * as an <code>int</code>. If the request does not have a header
     * of the specified name, this method returns -1. If the
     * header cannot be converted to an integer, this method
     * throws a <code>NumberFormatException</code>.
     * <p>
     * <p>The header name is case insensitive.
     *
     * @param name a <code>String</code> specifying the name
     *             of a request header
     * @return an integer expressing the value
     * of the request header or -1
     * if the request doesn't have a
     * header of this name
     * @throws NumberFormatException If the header value
     *                               can't be converted
     *                               to an <code>int</code>
     */
    int getIntHeader(String name);

    /**
     * Returns the name of the HTTP method with which this
     * request was made, for example, GET, POST, or PUT.
     * Same as the value of the CGI variable REQUEST_METHOD.
     *
     * @return a <code>String</code>
     * specifying the name
     * of the method with which
     * this request was made
     */
    String getMethod();

    /**
     * Returns the query string that is contained in the request
     * URL after the path. This method returns <code>null</code>
     * if the URL does not have a query string.
     *
     * @return a <code>String</code> containing the query
     * string or <code>null</code> if the URL
     * contains no query string. The value is not
     * decoded by the container.
     */
    String getQueryString();

    /**
     * Returns the part of this request's URL from the protocol
     * name up to the query string in the first line of the HTTP request.
     * The web container does not decode this String.
     * For example:
     * <p>
     * <table summary="Examples of Returned ValueUtil">
     * <tr align=left><th>First line of HTTP request      </th>
     * <th>     Returned Value</th>
     * <tr><td>POST /some/path.html HTTP/1.1<td><td>/some/path.html
     * <tr><td>GET http://foo.bar/a.html HTTP/1.0
     * <td><td>/a.html
     * <tr><td>HEAD /xyz?a=b HTTP/1.1<td><td>/xyz
     * </table>
     */
    String getRequestURI();

    String getPath();

    List<UploadFile> getUploadFiles();

    UploadFile getUploadFile(String name);

    List<UploadFile> getUploadFiles(String name);

    /**
     * Returns the current <code>HttpServerSession</code>
     * associated with this request or, if there is no
     * current session and <code>create</code> is true, returns
     * a new session.
     * <p>
     * <p>If <code>create</code> is <code>false</code>
     * and the request has no valid <code>HttpServerSession</code>,
     * this method returns <code>null</code>.
     * <p>
     * <p>To make sure the session is properly maintained,
     * you must call this method before
     * the response is committed. If the container is using cookies
     * to maintain session integrity and is asked to create a new session
     * when the response is committed, an IllegalStateException is thrown.
     *
     * @param create <code>true</code> to create
     *               a new session for this request if necessary;
     *               <code>false</code> to return <code>null</code>
     *               if there's no current session
     * @return the <code>HttpServerSession</code> associated
     * with this request or <code>null</code> if
     * <code>create</code> is <code>false</code>
     * and the request has no valid session
     * @see #getSession()
     */
    HttpServerSession getSession(boolean create);

    /**
     * Returns the current session associated with this request,
     * or if the request does not have a session, creates one.
     *
     * @return the <code>HttpServerSession</code> associated
     * with this request
     * @see #getSession(boolean)
     */
    HttpServerSession getSession();
}