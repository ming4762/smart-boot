package com.smart.auth.core.utils.request;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

/**
 * 用于匹配路径的httpservlet
 * @author zhongming4762
 * 2023/3/10
 */
public class MatcherHttpServletRequest implements HttpServletRequest {

    private final String httpMethod;

    private final String url;

    public MatcherHttpServletRequest(String httpMethod, String url) {
        this.httpMethod = httpMethod;
        this.url = url;
    }

    /**
     * Returns the name of the authentication scheme used to protect the
     * servlet. All servlet containers support basic, form and client
     * certificate authentication, and may additionally support digest
     * authentication. If the servlet is not authenticated <code>null</code> is
     * returned.
     * <p>
     * Same as the value of the CGI variable AUTH_TYPE.
     *
     * @return one of the static members BASIC_AUTH, FORM_AUTH, CLIENT_CERT_AUTH,
     * DIGEST_AUTH (suitable for == comparison) or the
     * container-specific string indicating the authentication scheme,
     * or <code>null</code> if the request was not authenticated.
     */
    @Override
    public String getAuthType() {
        return null;
    }

    /**
     * Returns an array containing all of the <code>Cookie</code> objects the
     * client sent with this request. This method returns <code>null</code> if
     * no cookies were sent.
     *
     * @return an array of all the <code>Cookies</code> included with this
     * request, or <code>null</code> if the request has no cookies
     */
    @Override
    public Cookie[] getCookies() {
        return new Cookie[0];
    }

    /**
     * Returns the value of the specified request header as a <code>long</code>
     * value that represents a <code>Date</code> object. Use this method with
     * headers that contain dates, such as <code>If-Modified-Since</code>.
     * <p>
     * The date is returned as the number of milliseconds since January 1, 1970
     * GMT. The header name is case insensitive.
     * <p>
     * If the request did not have a header of the specified name, this method
     * returns -1. If the header can't be converted to a date, the method throws
     * an <code>IllegalArgumentException</code>.
     *
     * @param name a <code>String</code> specifying the name of the header
     * @return a <code>long</code> value representing the date specified in the
     * header expressed as the number of milliseconds since January 1,
     * 1970 GMT, or -1 if the named header was not included with the
     * request
     * @throws IllegalArgumentException If the header value can't be converted to a date
     */
    @Override
    public long getDateHeader(String name) {
        return 0;
    }

    /**
     * Returns the value of the specified request header as a
     * <code>String</code>. If the request did not include a header of the
     * specified name, this method returns <code>null</code>. If there are
     * multiple headers with the same name, this method returns the first head
     * in the request. The header name is case insensitive. You can use this
     * method with any request header.
     *
     * @param name a <code>String</code> specifying the header name
     * @return a <code>String</code> containing the value of the requested
     * header, or <code>null</code> if the request does not have a
     * header of that name
     */
    @Override
    public String getHeader(String name) {
        return null;
    }

    /**
     * Returns all the values of the specified request header as an
     * <code>Enumeration</code> of <code>String</code> objects.
     * <p>
     * Some headers, such as <code>Accept-Language</code> can be sent by clients
     * as several headers each with a different value rather than sending the
     * header as a comma separated list.
     * <p>
     * If the request did not include any headers of the specified name, this
     * method returns an empty <code>Enumeration</code>. The header name is case
     * insensitive. You can use this method with any request header.
     *
     * @param name a <code>String</code> specifying the header name
     * @return an <code>Enumeration</code> containing the values of the requested
     * header. If the request does not have any headers of that name
     * return an empty enumeration. If the container does not allow
     * access to header information, return null
     */
    @Override
    public Enumeration<String> getHeaders(String name) {
        return null;
    }

    /**
     * Returns an enumeration of all the header names this request contains. If
     * the request has no headers, this method returns an empty enumeration.
     * <p>
     * Some servlet containers do not allow servlets to access headers using
     * this method, in which case this method returns <code>null</code>
     *
     * @return an enumeration of all the header names sent with this request; if
     * the request has no headers, an empty enumeration; if the servlet
     * container does not allow servlets to use this method,
     * <code>null</code>
     */
    @Override
    public Enumeration<String> getHeaderNames() {
        return null;
    }

    /**
     * Returns the value of the specified request header as an <code>int</code>.
     * If the request does not have a header of the specified name, this method
     * returns -1. If the header cannot be converted to an integer, this method
     * throws a <code>NumberFormatException</code>.
     * <p>
     * The header name is case insensitive.
     *
     * @param name a <code>String</code> specifying the name of a request header
     * @return an integer expressing the value of the request header or -1 if the
     * request doesn't have a header of this name
     * @throws NumberFormatException If the header value can't be converted to an
     *                               <code>int</code>
     */
    @Override
    public int getIntHeader(String name) {
        return 0;
    }

    /**
     * Returns the name of the HTTP method with which this request was made, for
     * example, GET, POST, or PUT. Same as the value of the CGI variable
     * REQUEST_METHOD.
     *
     * @return a <code>String</code> specifying the name of the method with
     * which this request was made
     */
    @Override
    public String getMethod() {
        return this.httpMethod;
    }

    /**
     * Returns any extra path information associated with the URL the client
     * sent when it made this request. The extra path information follows the
     * servlet path but precedes the query string and will start with a "/"
     * character.
     * <p>
     * This method returns <code>null</code> if there was no extra path
     * information.
     * <p>
     * The URL will be canonicalized as per section 3.5 of the specification
     * before the path information, if any, is extracted.
     *
     * @return a <code>String</code>, canonicalized by the web container,
     * specifying extra path information that comes after the servlet
     * path but before the query string in the request URL; or
     * {@code null} if the URL does not have any extra path information
     */
    @Override
    public String getPathInfo() {
        return null;
    }

    /**
     * Returns any extra path information after the servlet name but before the
     * query string, and translates it to a real path. Same as the value of the
     * CGI variable PATH_TRANSLATED.
     * <p>
     * If the URL does not have any extra path information, this method returns
     * <code>null</code> or the servlet container cannot translate the virtual
     * path to a real path for any reason (such as when the web application is
     * executed from an archive). The web container does not decode this string.
     *
     * @return a <code>String</code> specifying the real path, or
     * <code>null</code> if the URL does not have any extra path
     * information
     */
    @Override
    public String getPathTranslated() {
        return null;
    }

    /**
     * Returns the portion of the request URI that indicates the context of the
     * request. The context path always comes first in a request URI. The path
     * starts with a "/" character but does not end with a "/" character. For
     * servlets in the default (root) context, this method returns "". The
     * container does not decode this string.
     *
     * @return a <code>String</code> specifying the portion of the request URI
     * that indicates the context of the request
     */
    @Override
    public String getContextPath() {
        return null;
    }

    /**
     * Returns the query string that is contained in the request URL after the
     * path. This method returns <code>null</code> if the URL does not have a
     * query string. Same as the value of the CGI variable QUERY_STRING.
     *
     * @return a <code>String</code> containing the query string or
     * <code>null</code> if the URL contains no query string. The value
     * is not decoded by the container.
     */
    @Override
    public String getQueryString() {
        return null;
    }

    /**
     * Returns the login of the user making this request, if the user has been
     * authenticated, or <code>null</code> if the user has not been
     * authenticated. Whether the user name is sent with each subsequent request
     * depends on the browser and type of authentication. Same as the value of
     * the CGI variable REMOTE_USER.
     *
     * @return a <code>String</code> specifying the login of the user making
     * this request, or <code>null</code> if the user login is not known
     */
    @Override
    public String getRemoteUser() {
        return null;
    }

    /**
     * Returns a boolean indicating whether the authenticated user is included
     * in the specified logical "role". Roles and role membership can be defined
     * using deployment descriptors. If the user has not been authenticated, the
     * method returns <code>false</code>.
     *
     * @param role a <code>String</code> specifying the name of the role
     * @return a <code>boolean</code> indicating whether the user making this
     * request belongs to a given role; <code>false</code> if the user
     * has not been authenticated
     */
    @Override
    public boolean isUserInRole(String role) {
        return false;
    }

    /**
     * Returns a <code>java.security.Principal</code> object containing the name
     * of the current authenticated user. If the user has not been
     * authenticated, the method returns <code>null</code>.
     *
     * @return a <code>java.security.Principal</code> containing the name of the
     * user making this request; <code>null</code> if the user has not
     * been authenticated
     */
    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    /**
     * Returns the session ID specified by the client. This may not be the same
     * as the ID of the current valid session for this request. If the client
     * did not specify a session ID, this method returns <code>null</code>.
     *
     * @return a <code>String</code> specifying the session ID, or
     * <code>null</code> if the request did not specify a session ID
     * @see #isRequestedSessionIdValid
     */
    @Override
    public String getRequestedSessionId() {
        return null;
    }

    /**
     * Returns the part of this request's URL from the protocol name up to the
     * query string in the first line of the HTTP request. The web container
     * does not decode this String. For example:
     * <table>
     * <caption>Examples of Returned Values</caption>
     * <tr>
     * <th>First line of HTTP request</th>
     * <th>Returned Value</th>
     * <tr>
     * <td>POST /some/path.html HTTP/1.1
     * <td>
     * <td>/some/path.html
     * <tr>
     * <td>GET http://foo.bar/a.html HTTP/1.0
     * <td>
     * <td>/a.html
     * <tr>
     * <td>HEAD /xyz?a=b HTTP/1.1
     * <td>
     * <td>/xyz
     * </table>
     * <p>
     * To reconstruct a URL with a scheme and host, use
     * {@link #getRequestURL}.
     *
     * @return a <code>String</code> containing the part of the URL from the
     * protocol name up to the query string
     * @see #getRequestURL
     */
    @Override
    public String getRequestURI() {
        return this.url;
    }

    /**
     * Reconstructs the URL the client used to make the request. The returned
     * URL contains a protocol, server name, port number, and server path, but
     * it does not include query string parameters.
     * <p>
     * Because this method returns a <code>StringBuffer</code>, not a string,
     * you can modify the URL easily, for example, to append query parameters.
     * <p>
     * This method is useful for creating redirect messages and for reporting
     * errors.
     *
     * @return a <code>StringBuffer</code> object containing the reconstructed
     * URL
     */
    @Override
    public StringBuffer getRequestURL() {
        return null;
    }

    /**
     * Returns the part of this request's URL that calls the servlet. This path
     * starts with a "/" character and includes either the servlet name or a
     * path to the servlet, but does not include any extra path information or a
     * query string. Same as the value of the CGI variable SCRIPT_NAME.
     * <p>
     * The URL will be canonicalized as per section 3.5 of the specification
     * before the path information, if any, is extracted.
     * <p>
     * This method will return an empty string ("") if the servlet used to
     * process this request was matched using the "/*" pattern.
     *
     * @return a <code>String</code>, canonicalized by the web container,
     * containing the name or path of the servlet being called, as
     * specified in the request URL, or an empty string if the servlet
     * used to process the request is matched using the "/*" pattern.
     */
    @Override
    public String getServletPath() {
        return this.url;
    }

    /**
     * Returns the current <code>HttpSession</code> associated with this request
     * or, if there is no current session and <code>create</code> is true,
     * returns a new session.
     * <p>
     * If <code>create</code> is <code>false</code> and the request has no valid
     * <code>HttpSession</code>, this method returns <code>null</code>.
     * <p>
     * To make sure the session is properly maintained, you must call this
     * method before the response is committed. If the container is using
     * cookies to maintain session integrity and is asked to create a new
     * session when the response is committed, an IllegalStateException is
     * thrown.
     *
     * @param create <code>true</code> to create a new session for this request if
     *               necessary; <code>false</code> to return <code>null</code> if
     *               there's no current session
     * @return the <code>HttpSession</code> associated with this request or
     * <code>null</code> if <code>create</code> is <code>false</code>
     * and the request has no valid session
     * @see #getSession()
     */
    @Override
    public HttpSession getSession(boolean create) {
        return null;
    }

    /**
     * Returns the current session associated with this request, or if the
     * request does not have a session, creates one.
     *
     * @return the <code>HttpSession</code> associated with this request
     * @see #getSession(boolean)
     */
    @Override
    public HttpSession getSession() {
        return null;
    }

    /**
     * Changes the session ID of the session associated with this request. This
     * method does not create a new session object it only changes the ID of the
     * current session.
     *
     * @return the new session ID allocated to the session
     * @see HttpSessionIdListener
     * @since Servlet 3.1
     */
    @Override
    public String changeSessionId() {
        return null;
    }

    /**
     * Checks whether the requested session ID is still valid.
     *
     * @return <code>true</code> if this request has an id for a valid session
     * in the current session context; <code>false</code> otherwise
     * @see #getRequestedSessionId
     * @see #getSession
     */
    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    /**
     * Checks whether the requested session ID came in as a cookie.
     *
     * @return <code>true</code> if the session ID came in as a cookie;
     * otherwise, <code>false</code>
     * @see #getSession
     */
    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    /**
     * Checks whether the requested session ID came in as part of the request
     * URL.
     *
     * @return <code>true</code> if the session ID came in as part of a URL;
     * otherwise, <code>false</code>
     * @see #getSession
     */
    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    /**
     * Triggers the same authentication process as would be triggered if the
     * request is for a resource that is protected by a security constraint.
     *
     * @param response The response to use to return any authentication
     *                 challenge
     * @return <code>true</code> if the user is successfully authenticated and
     * <code>false</code> if not
     * @throws IOException           if the authentication process attempted to read from
     *                               the request or write to the response and an I/O error occurred
     * @throws IllegalStateException if the authentication process attempted to
     *                               write to the response after it had been committed
     * @throws ServletException      if the authentication failed and the caller is
     *                               expected to handle the failure
     * @since Servlet 3.0
     */
    @Override
    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        return false;
    }

    /**
     * Authenticate the provided user name and password and then associated the
     * authenticated user with the request.
     *
     * @param username The user name to authenticate
     * @param password The password to use to authenticate the user
     * @throws ServletException If any of {@link #getRemoteUser()},
     *                          {@link #getUserPrincipal()} or {@link #getAuthType()} are
     *                          non-null, if the configured authenticator does not support
     *                          user name and password authentication or if the
     *                          authentication fails
     * @since Servlet 3.0
     */
    @Override
    public void login(String username, String password) throws ServletException {

    }

    /**
     * Removes any authenticated user from the request.
     *
     * @throws ServletException If the logout fails
     * @since Servlet 3.0
     */
    @Override
    public void logout() throws ServletException {

    }

    /**
     * Return a collection of all uploaded Parts.
     *
     * @return A collection of all uploaded Parts.
     * @throws IOException           if an I/O error occurs
     * @throws IllegalStateException if size limits are exceeded or no multipart configuration is
     *                               provided
     * @throws ServletException      if the request is not multipart/form-data
     * @since Servlet 3.0
     */
    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return null;
    }

    /**
     * Gets the named Part or null if the Part does not exist. Triggers upload
     * of all Parts.
     *
     * @param name The name of the Part to obtain
     * @return The named Part or null if the Part does not exist
     * @throws IOException           if an I/O error occurs
     * @throws IllegalStateException if size limits are exceeded
     * @throws ServletException      if the request is not multipart/form-data
     * @since Servlet 3.0
     */
    @Override
    public Part getPart(String name) throws IOException, ServletException {
        return null;
    }

    /**
     * Start the HTTP upgrade process and create and instance of the provided
     * protocol handler class. The connection will be passed this instance once
     * the current request/response pair has completed processing. Calling this
     * method sets the response status to
     * {@link HttpServletResponse#SC_SWITCHING_PROTOCOLS}.
     *
     * @param httpUpgradeHandlerClass The class that implements the upgrade
     *                                handler
     * @return A newly created instance of the specified upgrade handler type
     * @throws IOException      if an I/O error occurred during the upgrade
     * @throws ServletException if the given httpUpgradeHandlerClass fails to be instantiated
     * @since Servlet 3.1
     */
    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> httpUpgradeHandlerClass) throws IOException, ServletException {
        return null;
    }

    /**
     * Returns the value of the named attribute as an <code>Object</code>, or
     * <code>null</code> if no attribute of the given name exists.
     * <p>
     * Attributes can be set two ways. The servlet container may set attributes
     * to make available custom information about a request. For example, for
     * requests made using HTTPS, the attribute
     * <code>jakarta.servlet.request.X509Certificate</code> can be used to
     * retrieve information on the certificate of the client. Attributes can
     * also be set programmatically using {@link ServletRequest#setAttribute}.
     * This allows information to be embedded into a request before a
     * {@link RequestDispatcher} call.
     * <p>
     * Attribute names should follow the same conventions as package names.
     * Names beginning with <code>java.*</code> and <code>javax.*</code> are
     * reserved for use by the Servlet specification. Names beginning with
     * <code>sun.*</code>, <code>com.sun.*</code>, <code>oracle.*</code> and
     * <code>com.oracle.*</code>) are reserved for use by Oracle Corporation.
     *
     * @param name a <code>String</code> specifying the name of the attribute
     * @return an <code>Object</code> containing the value of the attribute, or
     * <code>null</code> if the attribute does not exist
     */
    @Override
    public Object getAttribute(String name) {
        return null;
    }

    /**
     * Returns an <code>Enumeration</code> containing the names of the
     * attributes available to this request. This method returns an empty
     * <code>Enumeration</code> if the request has no attributes available to
     * it.
     *
     * @return an <code>Enumeration</code> of strings containing the names of the
     * request's attributes
     */
    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    /**
     * Returns the name of the character encoding used in the body of this
     * request. This method returns <code>null</code> if the no character
     * encoding has been specified. The following priority order is used to
     * determine the specified encoding:
     * <ol>
     * <li>per request</li>
     * <li>web application default via the deployment descriptor or
     *     {@link ServletContext#setRequestCharacterEncoding(String)}</li>
     * <li>container default via container specific configuration</li>
     * </ol>
     *
     * @return a <code>String</code> containing the name of the character
     * encoding, or <code>null</code> if the request does not specify a
     * character encoding
     */
    @Override
    public String getCharacterEncoding() {
        return null;
    }

    /**
     * Overrides the name of the character encoding used in the body of this
     * request. This method must be called prior to reading request parameters
     * or reading input using getReader().
     *
     * @param env a <code>String</code> containing the name of the character
     *            encoding.
     * @throws UnsupportedEncodingException if this is not a valid encoding
     */
    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {

    }

    /**
     * Returns the length, in bytes, of the request body and made available by
     * the input stream, or -1 if the length is not known. For HTTP servlets,
     * same as the value of the CGI variable CONTENT_LENGTH.
     *
     * @return an integer containing the length of the request body or -1 if the
     * length is not known or is greater than {@link Integer#MAX_VALUE}
     */
    @Override
    public int getContentLength() {
        return 0;
    }

    /**
     * Returns the length, in bytes, of the request body and made available by
     * the input stream, or -1 if the length is not known. For HTTP servlets,
     * same as the value of the CGI variable CONTENT_LENGTH.
     *
     * @return a long integer containing the length of the request body or -1 if
     * the length is not known
     * @since Servlet 3.1
     */
    @Override
    public long getContentLengthLong() {
        return 0;
    }

    /**
     * Returns the MIME type of the body of the request, or <code>null</code> if
     * the type is not known. For HTTP servlets, same as the value of the CGI
     * variable CONTENT_TYPE.
     *
     * @return a <code>String</code> containing the name of the MIME type of the
     * request, or null if the type is not known
     */
    @Override
    public String getContentType() {
        return null;
    }

    /**
     * Retrieves the body of the request as binary data using a
     * {@link ServletInputStream}. Either this method or {@link #getReader} may
     * be called to read the body, not both.
     *
     * @return a {@link ServletInputStream} object containing the body of the
     * request
     * @throws IllegalStateException if the {@link #getReader} method has already been called
     *                               for this request
     * @throws IOException           if an input or output exception occurred
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    /**
     * Returns the value of a request parameter as a <code>String</code>, or
     * <code>null</code> if the parameter does not exist. Request parameters are
     * extra information sent with the request. For HTTP servlets, parameters
     * are contained in the query string or posted form data.
     * <p>
     * You should only use this method when you are sure the parameter has only
     * one value. If the parameter might have more than one value, use
     * {@link #getParameterValues}.
     * <p>
     * If you use this method with a multivalued parameter, the value returned
     * is equal to the first value in the array returned by
     * <code>getParameterValues</code>.
     * <p>
     * If the parameter data was sent in the request body, such as occurs with
     * an HTTP POST request, then reading the body directly via
     * {@link #getInputStream} or {@link #getReader} can interfere with the
     * execution of this method.
     *
     * @param name a <code>String</code> specifying the name of the parameter
     * @return a <code>String</code> representing the single value of the
     * parameter
     * @see #getParameterValues
     */
    @Override
    public String getParameter(String name) {
        return null;
    }

    /**
     * Returns an <code>Enumeration</code> of <code>String</code> objects
     * containing the names of the parameters contained in this request. If the
     * request has no parameters, the method returns an empty
     * <code>Enumeration</code>.
     *
     * @return an <code>Enumeration</code> of <code>String</code> objects, each
     * <code>String</code> containing the name of a request parameter;
     * or an empty <code>Enumeration</code> if the request has no
     * parameters
     */
    @Override
    public Enumeration<String> getParameterNames() {
        return null;
    }

    /**
     * Returns an array of <code>String</code> objects containing all of the
     * values the given request parameter has, or <code>null</code> if the
     * parameter does not exist.
     * <p>
     * If the parameter has a single value, the array has a length of 1.
     *
     * @param name a <code>String</code> containing the name of the parameter
     *             whose value is requested
     * @return an array of <code>String</code> objects containing the parameter's
     * values
     * @see #getParameter
     */
    @Override
    public String[] getParameterValues(String name) {
        return new String[0];
    }

    /**
     * Returns a java.util.Map of the parameters of this request. Request
     * parameters are extra information sent with the request. For HTTP
     * servlets, parameters are contained in the query string or posted form
     * data.
     *
     * @return an immutable java.util.Map containing parameter names as keys and
     * parameter values as map values. The keys in the parameter map are
     * of type String. The values in the parameter map are of type
     * String array.
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        return null;
    }

    /**
     * Returns the name and version of the protocol the request uses in the form
     * <i>protocol/majorVersion.minorVersion</i>, for example, HTTP/1.1. For
     * HTTP servlets, the value returned is the same as the value of the CGI
     * variable <code>SERVER_PROTOCOL</code>.
     *
     * @return a <code>String</code> containing the protocol name and version
     * number
     */
    @Override
    public String getProtocol() {
        return null;
    }

    /**
     * Returns the name of the scheme used to make this request, for example,
     * <code>http</code>, <code>https</code>, or <code>ftp</code>. Different
     * schemes have different rules for constructing URLs, as noted in RFC 1738.
     *
     * @return a <code>String</code> containing the name of the scheme used to
     * make this request
     */
    @Override
    public String getScheme() {
        return null;
    }

    /**
     * Returns the host name of the server to which the request was sent. It is
     * the value of the part before ":" in the <code>Host</code> header value,
     * if any, or the resolved server name, or the server IP address.
     *
     * @return a <code>String</code> containing the name of the server
     */
    @Override
    public String getServerName() {
        return null;
    }

    /**
     * Returns the port number to which the request was sent. It is the value of
     * the part after ":" in the <code>Host</code> header value, if any, or the
     * server port where the client connection was accepted on.
     *
     * @return an integer specifying the port number
     */
    @Override
    public int getServerPort() {
        return 0;
    }

    /**
     * Retrieves the body of the request as character data using a
     * <code>BufferedReader</code>. The reader translates the character data
     * according to the character encoding used on the body. Either this method
     * or {@link #getInputStream} may be called to read the body, not both.
     *
     * @return a <code>BufferedReader</code> containing the body of the request
     * @throws UnsupportedEncodingException if the character set encoding used is not supported and
     *                                      the text cannot be decoded
     * @throws IllegalStateException        if {@link #getInputStream} method has been called on this
     *                                      request
     * @throws IOException                  if an input or output exception occurred
     * @see #getInputStream
     */
    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    /**
     * Returns the Internet Protocol (IP) address of the client or last proxy
     * that sent the request. For HTTP servlets, same as the value of the CGI
     * variable <code>REMOTE_ADDR</code>.
     *
     * @return a <code>String</code> containing the IP address of the client
     * that sent the request
     */
    @Override
    public String getRemoteAddr() {
        return null;
    }

    /**
     * Returns the fully qualified name of the client or the last proxy that
     * sent the request. If the engine cannot or chooses not to resolve the
     * hostname (to improve performance), this method returns the dotted-string
     * form of the IP address. For HTTP servlets, same as the value of the CGI
     * variable <code>REMOTE_HOST</code>.
     *
     * @return a <code>String</code> containing the fully qualified name of the
     * client
     */
    @Override
    public String getRemoteHost() {
        return null;
    }

    /**
     * Stores an attribute in this request. Attributes are reset between
     * requests. This method is most often used in conjunction with
     * {@link RequestDispatcher}.
     * <p>
     * Attribute names should follow the same conventions as package names.
     * Names beginning with <code>java.*</code> and <code>javax.*</code> are
     * reserved for use by the Servlet specification. Names beginning with
     * <code>sun.*</code>, <code>com.sun.*</code>, <code>oracle.*</code> and
     * <code>com.oracle.*</code>) are reserved for use by Oracle Corporation.
     * <br>
     * If the object passed in is null, the effect is the same as calling
     * {@link #removeAttribute}. <br>
     * It is warned that when the request is dispatched from the servlet resides
     * in a different web application by <code>RequestDispatcher</code>, the
     * object set by this method may not be correctly retrieved in the caller
     * servlet.
     *
     * @param name a <code>String</code> specifying the name of the attribute
     * @param o    the <code>Object</code> to be stored
     */
    @Override
    public void setAttribute(String name, Object o) {

    }

    /**
     * Removes an attribute from this request. This method is not generally
     * needed as attributes only persist as long as the request is being
     * handled.
     * <p>
     * Attribute names should follow the same conventions as package names.
     * Names beginning with <code>java.*</code> and <code>javax.*</code> are
     * reserved for use by the Servlet specification. Names beginning with
     * <code>sun.*</code>, <code>com.sun.*</code>, <code>oracle.*</code> and
     * <code>com.oracle.*</code>) are reserved for use by Oracle Corporation.
     *
     * @param name a <code>String</code> specifying the name of the attribute to
     *             remove
     */
    @Override
    public void removeAttribute(String name) {

    }

    /**
     * Returns the preferred <code>Locale</code> that the client will accept
     * content in, based on the Accept-Language header. If the client request
     * doesn't provide an Accept-Language header, this method returns the
     * default locale for the server.
     *
     * @return the preferred <code>Locale</code> for the client
     */
    @Override
    public Locale getLocale() {
        return null;
    }

    /**
     * Returns an <code>Enumeration</code> of <code>Locale</code> objects
     * indicating, in decreasing order starting with the preferred locale, the
     * locales that are acceptable to the client based on the Accept-Language
     * header. If the client request doesn't provide an Accept-Language header,
     * this method returns an <code>Enumeration</code> containing one
     * <code>Locale</code>, the default locale for the server.
     *
     * @return an <code>Enumeration</code> of preferred <code>Locale</code>
     * objects for the client
     */
    @Override
    public Enumeration<Locale> getLocales() {
        return null;
    }

    /**
     * Returns a boolean indicating whether this request was made using a secure
     * channel, such as HTTPS.
     *
     * @return a boolean indicating if the request was made using a secure
     * channel
     */
    @Override
    public boolean isSecure() {
        return false;
    }

    /**
     * Returns a {@link RequestDispatcher} object that acts as a wrapper for the
     * resource located at the given path. A <code>RequestDispatcher</code>
     * object can be used to forward a request to the resource or to include the
     * resource in a response. The resource can be dynamic or static.
     * <p>
     * The pathname specified may be relative, although it cannot extend outside
     * the current servlet context. If the path begins with a "/" it is
     * interpreted as relative to the current context root. This method returns
     * <code>null</code> if the servlet container cannot return a
     * <code>RequestDispatcher</code>.
     * <p>
     * The difference between this method and
     * {@link ServletContext#getRequestDispatcher} is that this method can take
     * a relative path.
     *
     * @param path a <code>String</code> specifying the pathname to the resource.
     *             If it is relative, it must be relative against the current
     *             servlet.
     * @return a <code>RequestDispatcher</code> object that acts as a wrapper for
     * the resource at the specified path, or <code>null</code> if the
     * servlet container cannot return a <code>RequestDispatcher</code>
     * @see RequestDispatcher
     * @see ServletContext#getRequestDispatcher
     */
    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }

    /**
     * Returns the Internet Protocol (IP) source port of the client or last
     * proxy that sent the request.
     *
     * @return an integer specifying the port number
     * @since Servlet 2.4
     */
    @Override
    public int getRemotePort() {
        return 0;
    }

    /**
     * Returns the host name of the Internet Protocol (IP) interface on which
     * the request was received.
     *
     * @return a <code>String</code> containing the host name of the IP on which
     * the request was received.
     * @since Servlet 2.4
     */
    @Override
    public String getLocalName() {
        return null;
    }

    /**
     * Returns the Internet Protocol (IP) address of the interface on which the
     * request was received.
     *
     * @return a <code>String</code> containing the IP address on which the
     * request was received.
     * @since Servlet 2.4
     */
    @Override
    public String getLocalAddr() {
        return null;
    }

    /**
     * Returns the Internet Protocol (IP) port number of the interface on which
     * the request was received.
     *
     * @return an integer specifying the port number
     * @since Servlet 2.4
     */
    @Override
    public int getLocalPort() {
        return 0;
    }

    /**
     * @return TODO
     * @since Servlet 3.0 TODO SERVLET3 - Add comments
     */
    @Override
    public ServletContext getServletContext() {
        return null;
    }

    /**
     * @return TODO
     * @throws IllegalStateException If async is not supported for this request
     * @since Servlet 3.0 TODO SERVLET3 - Add comments
     */
    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    /**
     * @param servletRequest  The ServletRequest with which to initialise the
     *                        asynchronous context
     * @param servletResponse The ServletResponse with which to initialise the
     *                        asynchronous context
     * @return TODO
     * @throws IllegalStateException If async is not supported for this request
     * @since Servlet 3.0 TODO SERVLET3 - Add comments
     */
    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }

    /**
     * @return TODO
     * @since Servlet 3.0 TODO SERVLET3 - Add comments
     */
    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    /**
     * @return TODO
     * @since Servlet 3.0 TODO SERVLET3 - Add comments
     */
    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    /**
     * Get the current AsyncContext.
     *
     * @return The current AsyncContext
     * @throws IllegalStateException if the request is not in asynchronous mode
     *                               (i.e. @link #isAsyncStarted() is {@code false})
     * @since Servlet 3.0
     */
    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    /**
     * @return TODO
     * @since Servlet 3.0 TODO SERVLET3 - Add comments
     */
    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }

    /**
     * Obtain a unique (within the lifetime of the Servlet container) identifier
     * string for this request.
     * <p>
     * There is no defined format for this string. The format is implementation
     * dependent.
     *
     * @return A unique identifier for the request
     * @since Servlet 6.0
     */
    @Override
    public String getRequestId() {
        return null;
    }

    /**
     * Obtain the request identifier for this request as defined by the protocol
     * in use. Note that some protocols do not define such an identifier.
     * <p>
     * Examples of protocol provided request identifiers include:
     * <dl>
     * <dt>HTTP 1.x</dt>
     * <dd>None, so the empty string should be returned</dd>
     * <dt>HTTP 2</dt>
     * <dd>The stream identifier</dd>
     * <dt>HTTP 3</dt>
     * <dd>The stream identifier</dd>
     * <dt>AJP</dt>
     * <dd>None, so the empty string should be returned</dd>
     * </dl>
     *
     * @return The request identifier if one is defined, otherwise an empty
     * string
     * @since Servlet 6.0
     */
    @Override
    public String getProtocolRequestId() {
        return null;
    }

    /**
     * Obtain details of the network connection to the Servlet container that is
     * being used by this request. The information presented may differ from
     * information presented elsewhere in the Servlet API as raw information is
     * presented without adjustments for, example, use of reverse proxies that
     * may be applied elsewhere in the Servlet API.
     *
     * @return The network connection details.
     * @since Servlet 6.0
     */
    @Override
    public ServletConnection getServletConnection() {
        return null;
    }
}
