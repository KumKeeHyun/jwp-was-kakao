package webserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HttpHeadersParser {

    public static final String HEADER_KV_DELIMITER = ": ";
    public static final String COOKIE_FIELD_DELIMITER = "; ";
    public static final String COOKIE_KV_DELIMITER = "=";

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
            String[] tokens = header.split(HEADER_KV_DELIMITER, 2);
            if (tokens[0].equals("Cookie")) {
                parseCookie(tokens[1]).forEach(headers::addCookie);
                continue;
            }
            headers.addHeader(tokens[0], tokens[1]);
        }
        return headers;
    }

    public static List<HttpCookie> parseCookie(String headerField) {
        return Arrays.stream(headerField.split(COOKIE_FIELD_DELIMITER))
                .map(token -> token.split(COOKIE_KV_DELIMITER, 2))
                .map(tokens -> new HttpCookie(tokens[0], tokens[1]))
                .collect(Collectors.toList());
    }
}
