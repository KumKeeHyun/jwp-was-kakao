package webserver.http;

import java.util.Map;
import java.util.function.Supplier;

public class HttpRequest {

    private final RequestLine requestLine;
    private final HttpHeaders headers;
    private final byte[] bodyContent;
    private Map<String, String> pathVariables;
    private Supplier<Session> getSessionFn = () -> null;
    private Runnable removeSessionFn = () -> {};

    public HttpRequest(HttpMethod method, String path, String version, Map<String, String> headers, byte[] bodyContent) {
        this(method, path, version, new HttpHeaders(headers), bodyContent);
    }

    public HttpRequest(HttpMethod method, String path, String version, HttpHeaders headers, byte[] bodyContent) {
        this(new RequestLine(method, path, version), headers, bodyContent);
    }

    public HttpRequest(RequestLine requestLine, HttpHeaders headers, byte[] bodyContent) {
        this.requestLine = requestLine;
        this.headers = headers;
        this.bodyContent = bodyContent;
    }

    public void setPathVariables(Map<String, String> pathVariables) {
        this.pathVariables = pathVariables;
    }

    public HttpMethod getMethod() {
        return requestLine.getMethod();
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public String getVersion() {
        return requestLine.getVersion();
    }

    public String getHeader(String key) {
        return headers.getFieldValue(key);
    }

    public HttpCookie getCookie(String key) {
        return headers.getCookie(key);
    }

    public String getParameter(String key) {
        return requestLine.getQueryParameter(key);
    }

    public String getPathVariable(String key) {
        if (pathVariables == null) {
            return "";
        }
        return pathVariables.getOrDefault(key, "");
    }

    public byte[] getBodyContent() {
        return bodyContent;
    }

    public void withSession(Supplier<Session> getSessionFn, Runnable remoteSessionFn) {
        this.getSessionFn = getSessionFn;
        this.removeSessionFn = remoteSessionFn;
    }

    public Session getSession() {
        return getSessionFn.get();
    }

    public void remoteSession() {
        removeSessionFn.run();
    }

}
