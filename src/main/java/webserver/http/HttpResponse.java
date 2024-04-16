package webserver.http;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResponse {

    private HttpStatus status;
    private final HttpHeaders headers;
    private byte[] content;
    private Map<String, Session> sessions;

    public HttpResponse() {
        this.headers = new HttpHeaders();
        this.sessions = new HashMap<>();
    }

    public void addHeader(String key, String value) {
        this.headers.addHeader(key, value);
    }

    public void contentType(MediaType mediaType) {
        addHeader("Content-Type", mediaType.getMediaType());
    }
  
    public void ok(byte[] content) {
        responseStatus(HttpStatus.OK);
        responseBody(content);
    }

    public void notFound(byte[] content) {
        responseStatus(HttpStatus.NOT_FOUND);
        responseBody(content);
    }

    public void redirectUrl(String url) {
        responseStatus(HttpStatus.FOUND);
        addHeader("Location", url);
    }

    private void responseStatus(HttpStatus status) {
        this.status = status;
    }

    private void responseBody(byte[] content) {
        this.content = content;
        addHeader("Content-Length", String.valueOf(content.length));
    }

    public void addCookie(HttpCookie cookie) {
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

    public void saveSession(String sessionName, Session session) {
        this.sessions.put(sessionName, session);
    }

    public Session getSession(String sessionName) {
        return sessions.get(sessionName);
    }
}
