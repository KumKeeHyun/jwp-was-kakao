package webserver.http;

public class RequestLine {

    private final HttpMethod method;
    private final RequestURI uri;
    private final String httpVersion;

    public RequestLine(HttpMethod method, String uri, String httpVersion) {
        this(method, RequestURIParser.parse(uri), httpVersion);
    }

    public RequestLine(HttpMethod method, RequestURI uri, String httpVersion) {
        this.method = method;
        this.uri = uri;
        this.httpVersion = httpVersion;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return uri.getPath();
    }

    public String getVersion() {
        return httpVersion;
    }

    public String getQueryParameter(String key) {
        return uri.getQueryParameters().get(key);
    }


}
