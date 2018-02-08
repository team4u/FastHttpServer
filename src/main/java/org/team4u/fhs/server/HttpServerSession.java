package org.team4u.fhs.server;

import java.io.Serializable;

public interface HttpServerSession extends Serializable {

    String SESSION_ID_NAME = "JSESSIONID";

    /**
     * Returns the time when this session was created, measured
     * in milliseconds since midnight January 1, 1970 GMT.
     *
     * @return a <code>long</code> specifying
     * when this session was created,
     * expressed in
     * milliseconds since 1/1/1970 GMT
     * @throws IllegalStateException if this method is called on an
     *                               invalidated session
     */
    long getCreationTime();


    /**
     * Returns a string containing the unique identifier assigned
     * to this session. The identifier is assigned
     * by the servlet container and is implementation dependent.
     *
     * @return a string specifying the identifier
     * assigned to this session
     */
    String getId();

    /**
     * Returns the maximum time interval, in seconds, that
     * the servlet container will keep this session open between
     * client accesses. After this interval, the servlet container
     * will invalidate the session.  The maximum time interval can be set
     * with the <code>setMaxInactiveInterval</code> method.
     * <p>
     * <p>A return value of zero or less indicates that the
     * session will never timeout.
     *
     * @return an integer specifying the number of
     * seconds this session remains open
     * between client requests
     * @see #setMaxInactiveInterval
     */
    int getMaxInactiveInterval();

    /**
     * Specifies the time, in seconds, between client requests before the
     * servlet container will invalidate this session.
     * <p>
     * <p>An <tt>interval</tt> value of zero or less indicates that the
     * session should never timeout.
     *
     * @param interval An integer specifying the number
     *                 of seconds
     */
    void setMaxInactiveInterval(int interval);

    /**
     * Returns the object bound with the specified name in this session, or
     * <code>null</code> if no object is bound under the name.
     *
     * @param name a string specifying the name of the object
     * @return the object with the specified name
     * @throws IllegalStateException if this method is called on an
     *                               invalidated session
     */
    Object getAttribute(String name);

    /**
     * Binds an object to this session, using the name specified.
     * If an object of the same name is already bound to the session,
     * the object is replaced.
     * <p>
     * <p>After this method executes, and if the new object
     * implements <code>HttpSessionBindingListener</code>,
     * the container calls
     * <code>HttpSessionBindingListener.valueBound</code>. The container then
     * notifies any <code>HttpSessionAttributeListener</code>s in the web
     * application.
     * <p>
     * <p>If an object was already bound to this session of this name
     * that implements <code>HttpSessionBindingListener</code>, its
     * <code>HttpSessionBindingListener.valueUnbound</code> method is called.
     * <p>
     * <p>If the value passed in is null, this has the same effect as calling
     * <code>removeAttribute()<code>.
     *
     * @param name  the name to which the object is bound;
     *              cannot be null
     * @param value the object to be bound
     * @throws IllegalStateException if this method is called on an
     *                               invalidated session
     */
    void setAttribute(String name, Object value);

    /**
     * Removes the object bound with the specified name from
     * this session. If the session does not have an object
     * bound with the specified name, this method does nothing.
     * <p>
     * <p>After this method executes, and if the object
     * implements <code>HttpSessionBindingListener</code>,
     * the container calls
     * <code>HttpSessionBindingListener.valueUnbound</code>. The container
     * then notifies any <code>HttpSessionAttributeListener</code>s in the web
     * application.
     *
     * @param name the name of the object to
     *             remove from this session
     * @throws IllegalStateException if this method is called on an
     *                               invalidated session
     */
    void removeAttribute(String name);


    /**
     * Invalidates this session then unbinds any objects bound
     * to it.
     *
     * @throws IllegalStateException if this method is called on an
     *                               already invalidated session
     */
    void invalidate();


    /**
     * Returns <code>true</code> if the client does not yet know about the
     * session or if the client chooses not to join the session.  For
     * example, if the server used only cookie-based sessions, and
     * the client had disabled the use of cookies, then a session would
     * be new on each request.
     *
     * @return <code>true</code> if the
     * server has created a session,
     * but the client has not yet joined
     * @throws IllegalStateException if this method is called on an
     *                               already invalidated session
     */
    boolean isNew();

    String changeId();
}

