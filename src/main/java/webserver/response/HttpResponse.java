package webserver.response;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private HttpStatus status;
    private final Map<String, String> headers;
    private byte[] content;

    public HttpResponse() {
        this.headers = new HashMap<>();
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public void contentType(MediaType mediaType) {
        addHeader("Content-Type", mediaType.getMediaType());
    }


    public void responseStatus(HttpStatus status) {
        this.status = status;
    }

    public void responseBody(byte[] content) {
        this.content = content;
        addHeader("Content-Length", String.valueOf(content.length));
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public byte[] getContent() {
        return content;
    }
}
