package webserver.request;

import java.util.HashMap;
import java.util.Map;

public class HttpHeaders {

    private final Map<String, String> fields;

    public HttpHeaders() {
        this(new HashMap<>());
    }
    
    public HttpHeaders(Map<String, String> fields) {
        this.fields = fields;
    }

    public int size() {
        return fields.size();
    }
    
    public void addHeader(String name, String value) {
        this.fields.put(name, value);
    }

    public String getFieldValue(String fieldName) {
        return fields.get(fieldName);
    }
}
