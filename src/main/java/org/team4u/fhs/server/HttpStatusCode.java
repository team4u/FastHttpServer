package org.team4u.fhs.server;

/**
 * @author Jay Wu
 */
public enum HttpStatusCode {

    CONTINUE(100, "Continue"),

    SWITCHING_PROTOCOLS(101, "Switching Protocols"),

    /**
     * 102 Processing (WebDAV, RFC2518)
     */
    PROCESSING(102, "Processing"),

    /**
     * 200 OK
     */
    OK(200, "OK"),

    /**
     * 201 Created
     */
    CREATED(201, "Created"),

    /**
     * 202 Accepted
     */
    ACCEPTED(202, "Accepted"),

    /**
     * 203 Non-Authoritative Information (since HTTP/1.1)
     */
    NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),

    /**
     * 204 No Content
     */
    NO_CONTENT(204, "No Content"),

    /**
     * 205 Reset Content
     */
    RESET_CONTENT(205, "Reset Content"),

    /**
     * 206 Partial Content
     */
    PARTIAL_CONTENT(206, "Partial Content"),

    /**
     * 207 Multi-Status (WebDAV, RFC2518)
     */
    MULTI_STATUS(207, "Multi-Status"),

    /**
     * 300 Multiple Choices
     */
    MULTIPLE_CHOICES(300, "Multiple Choices"),

    /**
     * 301 Moved Permanently
     */
    MOVED_PERMANENTLY(301, "Moved Permanently"),

    /**
     * 302 Found
     */
    FOUND(302, "Found"),

    /**
     * 303 See Other (since HTTP/1.1)
     */
    SEE_OTHER(303, "See Other"),

    /**
     * 304 Not Modified
     */
    NOT_MODIFIED(304, "Not Modified"),

    /**
     * 305 Use Proxy (since HTTP/1.1)
     */
    USE_PROXY(305, "Use Proxy"),

    /**
     * 307 Temporary Redirect (since HTTP/1.1)
     */
    TEMPORARY_REDIRECT(307, "Temporary Redirect"),

    /**
     * 400 Bad Request
     */
    BAD_REQUEST(400, "Bad Request"),

    /**
     * 401 Unauthorized
     */
    UNAUTHORIZED(401, "Unauthorized"),

    /**
     * 402 Payment Required
     */
    PAYMENT_REQUIRED(402, "Payment Required"),

    /**
     * 403 Forbidden
     */
    FORBIDDEN(403, "Forbidden"),

    /**
     * 404 Not Found
     */
    NOT_FOUND(404, "Not Found"),

    /**
     * 405 Method Not Allowed
     */
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

    /**
     * 406 Not Acceptable
     */
    NOT_ACCEPTABLE(406, "Not Acceptable"),

    /**
     * 407 Proxy Authentication Required
     */
    PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),

    /**
     * 408 Request Timeout
     */
    REQUEST_TIMEOUT(408, "Request Timeout"),

    /**
     * 409 Conflict
     */
    CONFLICT(409, "Conflict"),

    /**
     * 410 Gone
     */
    GONE(410, "Gone"),

    /**
     * 411 Length Required
     */
    LENGTH_REQUIRED(411, "Length Required"),

    /**
     * 412 Precondition Failed
     */
    PRECONDITION_FAILED(412, "Precondition Failed"),

    /**
     * 413 Request Entity Too Large
     */
    REQUEST_ENTITY_TOO_LARGE(413, "Request Entity Too Large"),

    /**
     * 414 Request-URI Too Long
     */
    REQUEST_URI_TOO_LONG(414, "Request-URI Too Long"),

    /**
     * 415 Unsupported Media Type
     */
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),

    /**
     * 416 Requested Range Not Satisfiable
     */
    REQUESTED_RANGE_NOT_SATISFIABLE(416, "Requested Range Not Satisfiable"),

    /**
     * 417 Expectation Failed
     */
    EXPECTATION_FAILED(417, "Expectation Failed"),

    /**
     * 421 Misdirected Request
     * <p>
     * <a href="https://tools.ietf.org/html/draft-ietf-httpbis-http2-15#section-9.1.2">421 Status Code</a>
     */
    MISDIRECTED_REQUEST(421, "Misdirected Request"),

    /**
     * 422 Unprocessable Entity (WebDAV, RFC4918)
     */
    UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),

    /**
     * 423 Locked (WebDAV, RFC4918)
     */
    LOCKED(423, "Locked"),

    /**
     * 424 Failed Dependency (WebDAV, RFC4918)
     */
    FAILED_DEPENDENCY(424, "Failed Dependency"),

    /**
     * 425 Unordered Collection (WebDAV, RFC3648)
     */
    UNORDERED_COLLECTION(425, "Unordered Collection"),

    /**
     * 426 Upgrade Required (RFC2817)
     */
    UPGRADE_REQUIRED(426, "Upgrade Required"),

    /**
     * 428 Precondition Required (RFC6585)
     */
    PRECONDITION_REQUIRED(428, "Precondition Required"),

    /**
     * 429 Too Many Requests (RFC6585)
     */
    TOO_MANY_REQUESTS(429, "Too Many Requests"),

    /**
     * 431 Request Header Fields Too Large (RFC6585)
     */
    REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"),

    /**
     * 500 Internal Server Error
     */
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

    /**
     * 501 Not Implemented
     */
    NOT_IMPLEMENTED(501, "Not Implemented"),

    /**
     * 502 Bad Gateway
     */
    BAD_GATEWAY(502, "Bad Gateway"),

    /**
     * 503 Service Unavailable
     */
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),

    /**
     * 504 Gateway Timeout
     */
    GATEWAY_TIMEOUT(504, "Gateway Timeout"),

    /**
     * 505 HTTP Version Not Supported
     */
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported"),

    /**
     * 506 Variant Also Negotiates (RFC2295)
     */
    VARIANT_ALSO_NEGOTIATES(506, "Variant Also Negotiates"),

    /**
     * 507 Insufficient Storage (WebDAV, RFC4918)
     */
    INSUFFICIENT_STORAGE(507, "Insufficient Storage"),

    /**
     * 510 Not Extended (RFC2774)
     */
    NOT_EXTENDED(510, "Not Extended"),

    /**
     * 511 Network Authentication Required (RFC6585)
     */
    NETWORK_AUTHENTICATION_REQUIRED(511, "Network Authentication Required");

    private int code;
    private String reasonPhrase;

    HttpStatusCode(int code, String reasonPhrase) {
        this.code = code;
        this.reasonPhrase = reasonPhrase;
    }

    public int code() {
        return code;
    }

    public String reasonPhrase() {
        return reasonPhrase;
    }
}