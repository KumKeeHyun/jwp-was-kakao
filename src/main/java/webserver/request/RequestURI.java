package webserver.request;

import java.util.Collections;
import java.util.Map;

public class RequestURI {

    private final String scheme;
    private final String host;
    private final String path;
    private final Map<String, String> queryParameters;

    public RequestURI(String scheme, String host, String path, Map<String, String> queryParameters) {
        this.scheme = scheme;
        this.host = host;
        this.path = path;
        this.queryParameters = Collections.unmodifiableMap(queryParameters);
    }

    public String getScheme() {
        return scheme;
    }

    public String getHost() {
        return host;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getQueryParameters() {
        return queryParameters;
    }
}
