package webserver.http;

import java.io.BufferedReader;
import java.io.IOException;

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
                HttpCookie cookie = parseCookie(tokens[1]);
                headers.addCookie(cookie);
                continue;
            }
            headers.addHeader(tokens[0], tokens[1]);
        }
        return headers;
    }

    public static HttpCookie parseCookie(String headerField) {
        String[] tokens = headerField.split(COOKIE_FIELD_DELIMITER);
        if (tokens.length < 1) {
            return null;
        }
        String[] kvTokens = tokens[0].split(COOKIE_KV_DELIMITER);
        HttpCookie cookie = new HttpCookie(kvTokens[0], kvTokens[1]);
        for (int i = 1; i < tokens.length; i++) {
            String[] attrTokens = tokens[i].split("=");
            cookie.setAttribute(attrTokens[0], attrTokens[1]);
        }
        return cookie;
    }
}
