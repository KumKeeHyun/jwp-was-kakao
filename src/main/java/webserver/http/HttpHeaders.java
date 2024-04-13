package webserver.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HttpHeaders {

    private final Map<String, String> fields;
    private final Map<String, HttpCookie> cookies;

    public HttpHeaders() {
        this(new HashMap<>());
    }
    
    public HttpHeaders(Map<String, String> fields) {
        this(fields, new ArrayList<>());
    }

    public HttpHeaders(Map<String, String> fields, List<HttpCookie> cookies) {
        this.fields = fields;
        this.cookies = cookies.stream()
                .collect(Collectors.toMap(HttpCookie::getKey, Function.identity()));
    }

    public int size() {
        return fields.size();
    }
    
    public void addHeader(String name, String value) {
        this.fields.put(name, value);
    }

    public void addCookie(HttpCookie cookie) {
        cookies.put(cookie.getKey(), cookie);
    }

    public HttpCookie getCookie(String key) {
        return cookies.get(key);
    }

    public List<Map.Entry<String, String>> getAllFields() {
        List<Map.Entry<String, String>> entries = new ArrayList<>(fields.entrySet());
        cookies.values()
                .stream()
                .map(cookie -> Map.entry("Set-Cookie", cookie.toHeaderValue()))
                .forEach(entries::add);
        return entries;
    }

    public String getFieldValue(String fieldName) {
        return fields.get(fieldName);
    }
}
