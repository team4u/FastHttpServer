package org.team4u.fhs.server;

import cn.hutool.core.lang.Assert;

import java.io.Serializable;
import java.util.Locale;

/**
 * @author Jay Wu
 */
public class Cookie implements Cloneable, Serializable {

    private static final long serialVersionUID = -6454587001725327448L;
    //
    // The value of the cookie itself.
    //
    private String name;    // NAME= ... "$Name" style is reserved
    private String value;    // value of NAME

    //
    // Attributes encoded in the header's cookie fields.
    //
    private String comment;    // ;Comment=VALUE ... describes cookie's use
    // ;Discard ... implied by maxAge < 0
    private String domain;    // ;Domain=VALUE ... domain that sees cookie
    private int maxAge = -1;    // ;Max-Age=VALUE ... cookies auto-expire
    private String path;    // ;Path=VALUE ... URLs that see the cookie
    private boolean secure;    // ;Secure ... e.g. use SSL
    private int version = 0;    // ;Version=1 ... means RFC 2109++ style
    private boolean isHttpOnly = false;

    /**
     * Constructs a cookie with the specified name and value.
     * <p>
     * <p>The name must conform to RFC 2109. However, vendors may
     * provide a configuration option that allows cookie names conforming
     * to the original Netscape Cookie Specification to be accepted.
     * <p>
     * <p>The name of a cookie cannot be changed once the cookie has
     * been created.
     * <p>
     * <p>The value can be anything the server chooses to send. Its
     * value is probably of interest only to the server. The cookie's
     * value can be changed after creation with the
     * <code>setValue</code> method.
     * <p>
     * <p>By default, cookies are created according to the Netscape
     * cookie specification. The version can be changed with the
     * <code>setVersion</code> method.
     *
     * @param name  the name of the cookie
     * @param value the value of the cookie
     * @throws IllegalArgumentException if the cookie name is null or
     *                                  empty or contains any illegal characters (for example, a comma,
     *                                  space, or semicolon) or matches a token reserved for use by the
     *                                  cookie protocol
     * @see #setValue
     * @see #setVersion
     */
    public Cookie(String name, String value) {
        Assert.notEmpty(name, "name is required");
        this.name = name;
        this.value = value;
    }

    /**
     * Returns the comment describing the purpose of this cookie, or
     * <code>null</code> if the cookie has no comment.
     *
     * @return the comment of the cookie, or <code>null</code> if unspecified
     * @see #setComment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Specifies a comment that describes a cookie's purpose.
     * The comment is useful if the browser presents the cookie
     * to the user. Comments
     * are not supported by Netscape Version 0 cookies.
     *
     * @param purpose a <code>String</code> specifying the comment
     *                to display to the user
     * @see #getComment
     */
    public void setComment(String purpose) {
        comment = purpose;
    }

    /**
     * Gets the domain name of this Cookie.
     * <p>
     * <p>Domain names are formatted according to RFC 2109.
     *
     * @return the domain name of this Cookie
     * @see #setDomain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Specifies the domain within which this cookie should be presented.
     * <p>
     * <p>The form of the domain name is specified by RFC 2109. A domain
     * name begins with a dot (<code>.foo.com</code>) and means that
     * the cookie is visible to servers in a specified Domain Name System
     * (DNS) zone (for example, <code>www.foo.com</code>, but not
     * <code>a.b.foo.com</code>). By default, cookies are only returned
     * to the server that sent them.
     *
     * @param domain the domain name within which this cookie is visible;
     *               form is according to RFC 2109
     * @see #getDomain
     */
    public void setDomain(String domain) {
        if (domain == null) {
            return;
        }

        this.domain = domain.toLowerCase(Locale.ENGLISH); // IE allegedly needs this
    }

    /**
     * Gets the maximum age in seconds of this Cookie.
     * <p>
     * <p>By default, <code>-1</code> is returned, which indicates that
     * the cookie will persist until browser shutdown.
     *
     * @return an integer specifying the maximum age of the
     * cookie in seconds; if negative, means
     * the cookie persists until browser shutdown
     * @see #setMaxAge
     */
    public int getMaxAge() {
        return maxAge;
    }

    /**
     * Sets the maximum age in seconds for this Cookie.
     * <p>
     * <p>A positive value indicates that the cookie will expire
     * after that many seconds have passed. Note that the value is
     * the <i>maximum</i> age when the cookie will expire, not the cookie's
     * current age.
     * <p>
     * <p>A negative value means
     * that the cookie is not stored persistently and will be deleted
     * when the Web browser exits. A zero value causes the cookie
     * to be deleted.
     *
     * @param expiry an integer specifying the maximum age of the
     *               cookie in seconds; if negative, means
     *               the cookie is not stored; if zero, deletes
     *               the cookie
     * @see #getMaxAge
     */
    public void setMaxAge(int expiry) {
        maxAge = expiry;
    }

    /**
     * Returns the path on the server
     * to which the browser returns this cookie. The
     * cookie is visible to all subpaths on the server.
     */
    public String getPath() {
        return path;
    }

    /**
     * Specifies a path for the cookie
     * to which the client should return the cookie.
     * <p>
     * <p>Consult RFC 2109 (available on the Internet) for more
     * information on setting path names for cookies.
     *
     * @param uri a <code>String</code> specifying a path
     * @see #getPath
     */
    public void setPath(String uri) {
        path = uri;
    }

    /**
     * Returns <code>true</code> if the browser is sending cookies
     * only over a secure protocol, or <code>false</code> if the
     * browser can send cookies using any protocol.
     *
     * @return <code>true</code> if the browser uses a secure protocol,
     * <code>false</code> otherwise
     * @see #setSecure
     */
    public boolean getSecure() {
        return secure;
    }

    /**
     * Indicates to the browser whether the cookie should only be sent
     * using a secure protocol, such as HTTPS or SSL.
     * <p>
     * <p>The default value is <code>false</code>.
     *
     * @param flag if <code>true</code>, sends the cookie from the browser
     *             to the server only when using a secure protocol; if <code>false</code>,
     *             sent on any protocol
     * @see #getSecure
     */
    public void setSecure(boolean flag) {
        secure = flag;
    }

    /**
     * Returns the name of the cookie. The name cannot be changed after
     * creation.
     *
     * @return the name of the cookie
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the current value of this Cookie.
     *
     * @return the current value of this Cookie
     * @see #setValue
     */
    public String getValue() {
        return value;
    }

    /**
     * Assigns a new value to this Cookie.
     * <p>
     * <p>If you use a binary value, you may want to use BASE64 encoding.
     * <p>
     * <p>With Version 0 cookies, ValueUtil should not contain white
     * space, brackets, parentheses, equals signs, commas,
     * double quotes, slashes, question marks, at signs, colons,
     * and semicolons. Empty ValueUtil may not behave the same way
     * on all browsers.
     *
     * @param newValue the new value of the cookie
     * @see #getValue
     */
    public void setValue(String newValue) {
        value = newValue;
    }

    /**
     * Returns the version of the protocol this cookie complies
     * with. Version 1 complies with RFC 2109,
     * and version 0 complies with the original
     * cookie specification drafted by Netscape. Cookies provided
     * by a browser use and identify the browser's cookie version.
     *
     * @return 0 if the cookie complies with the
     * original Netscape specification; 1
     * if the cookie complies with RFC 2109
     * @see #setVersion
     */
    public int getVersion() {
        return version;
    }

    /**
     * Sets the version of the cookie protocol that this Cookie complies
     * with.
     * <p>
     * <p>Version 0 complies with the original Netscape cookie
     * specification. Version 1 complies with RFC 2109.
     * <p>
     * <p>Since RFC 2109 is still somewhat new, consider
     * version 1 as experimental; do not use it yet on production sites.
     *
     * @param v 0 if the cookie should comply with the original Netscape
     *          specification; 1 if the cookie should comply with RFC 2109
     * @see #getVersion
     */
    public void setVersion(int v) {
        version = v;
    }

    /**
     * Overrides the standard <code>java.lang.Object.clone</code>
     * method to return a copy of this Cookie.
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Checks whether this Cookie has been marked as <i>HttpOnly</i>.
     *
     * @return true if this Cookie has been marked as <i>HttpOnly</i>,
     * false otherwise
     */
    public boolean isHttpOnly() {
        return isHttpOnly;
    }

    /**
     * Marks or unmarks this Cookie as <i>HttpOnly</i>.
     * <p>
     * <p>If <tt>isHttpOnly</tt> is set to <tt>true</tt>, this cookie is
     * marked as <i>HttpOnly</i>, by adding the <tt>HttpOnly</tt> attribute
     * to it.
     * <p>
     * <p><i>HttpOnly</i> cookies are not supposed to be exposed to
     * client-side scripting code, and may therefore help mitigate certain
     * kinds of cross-site scripting attacks.
     *
     * @param isHttpOnly true if this cookie is to be marked as
     *                   <i>HttpOnly</i>, false otherwise
     */
    public void setHttpOnly(boolean isHttpOnly) {
        this.isHttpOnly = isHttpOnly;
    }
}