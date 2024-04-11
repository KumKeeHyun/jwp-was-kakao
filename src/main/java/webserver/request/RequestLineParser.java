package webserver.request;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestLineParser {

    public static final String SPACE = " ";

    /*
    parse = Method SP Request-URI SP HTTP-Version CRLF
    */
    public static RequestLine parse(BufferedReader reader) throws IOException {
        String startLine = reader.readLine();
        String[] tokens = startLine.split(SPACE, 3);
        if (tokens.length != 3) {
            throw new IllegalArgumentException("잘못된 형식의 RequestLine 입니다");
        }

        HttpMethod method = HttpMethod.valueOf(tokens[0]);
        RequestURI uri = RequestURIParser.parse(tokens[1]);
        String httpVersion = tokens[2];
        return new RequestLine(method, uri, httpVersion);
    }
}
