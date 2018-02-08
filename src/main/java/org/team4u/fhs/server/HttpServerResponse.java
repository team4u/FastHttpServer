package org.team4u.fhs.server;


import java.io.File;
import java.util.Collection;
import java.util.Set;

/**
 * @author Jay Wu
 */
public interface HttpServerResponse {

    /**
     * Sets the length of the content body in the response
     *
     * @param len an integer specifying the length of the
     *            content being returned to the client; sets
     *            the Content-Length header
     */

    void setContentLength(long len);

    /**
     * Returns the content type used for the MIME body
     * sent in this response. The content type proper must
     * have been specified using {@link #setContentType}
     * before the response is committed. If no content type
     * has been specified, this method returns null.
     *
     * @return a <code>String</code> specifying the
     * content type, for example,
     * <code>text/html; charset=UTF-8</code>,
     * or null
     */
    String getContentType();

    /**
     * Sets the content type of the response being sent to
     * the client, if the response has not been committed yet.
     * The given content type may include a character encoding
     * specification, for example, <code>text/html;charset=UTF-8</code>.
     * The response's character encoding is only set from the given
     * content type if this method is called before <code>getWriter</code>
     * is called.
     * <p>This method may be called repeatedly to change content type and
     * character encoding.
     * This method has no effect if called after the response
     * has been committed. It does not set the response's character
     * encoding if it is called after <code>getWriter</code>
     * has been called or after the response has been committed.
     *
     * @param type a <code>String</code> specifying the MIME
     *             type of the content
     */

    void setContentType(String type);

    /**
     * Adds the specified cookie to the response.  This method can be called
     * multiple times to set more than one cookie.
     */
    HttpServerResponse addCookie(Cookie cookie);

    /**
     * Returns a boolean indicating whether the named response header
     * has already been set.
     *
     * @param name the header name
     * @return <code>true</code> if the named response header
     * has already been set;
     * <code>false</code> otherwise
     */
    boolean containsHeader(String name);

    /**
     * Sets a response header with the given name and value.
     * If the header had already been set, the new value overwrites the
     * previous one.  The <code>containsHeader</code> method can be
     * used to test for the presence of a header before setting its
     * value.
     *
     * @param name  the name of the header
     * @param value the header value  If it contains octet string,
     *              it should be encoded according to RFC 2047
     *              (http://www.ietf.org/rfc/rfc2047.txt)
     * @see #containsHeader
     * @see #addHeader
     */
    HttpServerResponse setHeader(String name, Object value);

    /**
     * Adds a response header with the given name and value.
     * This method allows response headers to have multiple ValueUtil.
     *
     * @param name  the name of the header
     * @param value the additional header value   If it contains
     *              octet string, it should be encoded
     *              according to RFC 2047
     *              (http://www.ietf.org/rfc/rfc2047.txt)
     * @see #setHeader
     */
    HttpServerResponse addHeader(String name, Object value);

    HttpServerResponse sendError(int status);

    /**
     * Gets the current status code of this response.
     *
     * @return the current status code of this response
     */
    int getStatus();

    /**
     * Sets the status code for this response.
     */
    HttpServerResponse setStatus(int status);

    /**
     * Gets the value of the response header with the given name.
     */
    String getHeader(String name);

    /**
     * Gets the ValueUtil of the response header with the given name.
     */
    Collection<String> getHeaders(String name);

    /**
     * Gets the names of the headers of this response.
     */
    Set<String> getHeaderNames();

    HttpServerResponse write(String data);

    HttpServerResponse write(byte[] data);

    HttpServerResponse write(File file);

    HttpServerResponse flush();

    /**
     * Returns a boolean indicating if the response has been
     * committed.  A committed response has already had its status
     * code and headers written.
     *
     * @return a boolean indicating if the response has been
     * committed
     */
    boolean isCommitted();
}