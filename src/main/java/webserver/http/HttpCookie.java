package webserver.http;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
        return attributes.get("Path");
    }

    public void setPath(String path) {
        setAttribute("Path", path);
    }

    public String getDomain() {
        return attributes.get("Domain");
    }

    public void setDomain(String domain) {
        setAttribute("Domain", domain);
    }

    public LocalDateTime getExpires() {
        String expires = attributes.get("Expires");
        if (expires == null || expires.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(expires, DateTimeFormatter.RFC_1123_DATE_TIME);
    }

    public void setExpires(LocalDateTime expireDate) {
        setAttribute("Expires", expireDate.format(DateTimeFormatter.RFC_1123_DATE_TIME));
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
