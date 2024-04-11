package webserver.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

public class HttpResponseRenderer {

    private static final String CRLF = "\r\n";
    private static final String STATUS_LINE_FMT = "HTTP/1.1 %d %s" + CRLF;
    private static final String HEADER_FMT = "%s: %s" + CRLF;

    public void render(DataOutputStream dos, HttpResponse response) throws IOException {
        renderStatusLine(dos, response.getStatus());
        renderHeaders(dos, response.getHeaders());
        renderBody(dos, response);
        dos.flush();
    }

    private void renderStatusLine(DataOutputStream dos, HttpStatus status) throws IOException {
        dos.writeBytes(String.format(STATUS_LINE_FMT, status.getCode(), status.getReasonPhrase()));
    }

    private static void renderHeaders(DataOutputStream dos, Map<String, String> headers) throws IOException {
        for (Entry<String, String> header : headers.entrySet()) {
            dos.writeBytes(String.format(HEADER_FMT, header.getKey(), header.getValue()));
        }
        dos.writeBytes(CRLF);
    }

    private static void renderBody(DataOutputStream dos, HttpResponse response) throws IOException {
        byte[] body = response.getContent();
        if (body != null && body.length != 0) {
            dos.write(body, 0, body.length);
        }
    }

}
