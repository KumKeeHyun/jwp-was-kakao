package webserver.http;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpHeadersParser {

    public static final String COLON = ": ";

    /*
    parse = *(message-header CRLF)
            CRLF
    */
    public static HttpHeaders parse(BufferedReader reader) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        String header;
        while ((header = reader.readLine()) != null) {
            if (header.isEmpty()) {
                break;
            }
            String[] tokens = header.split(COLON, 2);
            headers.addHeader(tokens[0], tokens[1]);
        }
        return headers;
    }
}
