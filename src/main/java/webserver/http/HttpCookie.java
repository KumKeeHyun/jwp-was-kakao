package webserver.http;

import java.util.HashMap;
import java.util.Map;

public class HttpCookie {

    private final String key;
    private final String value;

    private final Map<String, String> attributes = new HashMap<>();

    public HttpCookie(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getPath() {
        return attributes.get("path");
    }

    public void setPath(String path) {
        setAttribute("path", path);
    }

    public void setAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public String toHeaderValue() {
        StringBuilder builder = new StringBuilder();
        builder.append(key).append("=").append(value);
        attributes
                .forEach((key1, value1) ->
                        builder.append("; ").append(key1).append("=").append(value1));
        return builder.toString();
    }

}
