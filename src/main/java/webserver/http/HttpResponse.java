package webserver.http;

import java.util.List;
import java.util.Map;

public class HttpResponse {

    private HttpStatus status;
    private final HttpHeaders headers;
    private byte[] content;
    private Session session;

    public HttpResponse() {
        this.headers = new HttpHeaders();
    }

    public void addHeader(String key, String value) {
        this.headers.addHeader(key, value);
    }

    public void contentType(MediaType mediaType) {
        addHeader("Content-Type", mediaType.getMediaType());
    }

    public void redirectUrl(String url) {
        responseStatus(HttpStatus.FOUND);
        addHeader("Location", url);
    }

    public void responseStatus(HttpStatus status) {
        this.status = status;
    }

    public void responseBody(byte[] content) {
        this.content = content;
        addHeader("Content-Length", String.valueOf(content.length));
    }

    public void setCookie(HttpCookie cookie) {
        headers.addCookie(cookie);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public List<Map.Entry<String, String>> getHeaders() {
        return headers.getAllFields();
    }

    public byte[] getContent() {
        return content;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }
}
