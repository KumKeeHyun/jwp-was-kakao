package webserver.http;

import utils.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpRequestParser {


    /*
    parse = start-line
            *(message-header CRLF)
            CRLF
            [ message-body ]
    start-line = Method SP Request-URI SP HTTP-Version CRLF
     */
    public HttpRequest parseRequest(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        RequestLine requestLine = RequestLineParser.parse(reader);
        HttpHeaders headers = HttpHeadersParser.parse(reader);
        byte[] bodyContent = readBodyIfRequire(headers, reader);

        return new HttpRequest(requestLine, headers, bodyContent);
    }

    private byte[] readBodyIfRequire(HttpHeaders headers, BufferedReader reader) throws IOException {
        byte[] bodyContent = null;
        if (requireReadBody(headers)) {
            int contentLength = Integer.parseInt(headers.getFieldValue("Content-Length"));
            bodyContent = IOUtils.readData(reader, contentLength).getBytes();
        }
        return bodyContent;
    }

    private boolean requireReadBody(HttpHeaders headers) {
        return headers.getFieldValue("Content-Length") != null;
    }

}
