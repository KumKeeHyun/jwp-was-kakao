package webserver.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

public class HttpResponseRenderer {

    // TODO: 리펙토링
    public void render(DataOutputStream dos, HttpResponse response) throws IOException {
        HttpStatus status = response.getStatus();
        dos.writeBytes(String.format("HTTP/1.1 %d %s \r\n", status.getCode(), status.getReasonPhrase()));

        Map<String, String> headers = response.getHeaders();
        for (Entry<String, String> entry : headers.entrySet()) {
            String s = entry.getKey() + ":" + entry.getValue() + "\r\n";
            dos.writeBytes(s);
        }
        dos.writeBytes("\r\n");

        byte[] body = response.getContent();
        if (body != null && body.length != 0) {
            dos.write(body, 0, body.length);
        }
        dos.flush();
    }
}
