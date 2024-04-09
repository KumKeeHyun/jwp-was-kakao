package webserver.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import utils.IOUtils;

public class HttpRequestParser {

    // TODO: 리펙토링
    public HttpRequest parseRequest(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String requestLine = reader.readLine();
        String[] tokens = requestLine.split(" ");
        HttpMethod method = HttpMethod.valueOf(tokens[0]);

        String pathToken = tokens[1];
        String path = pathToken.split("\\?")[0];

        String version = tokens[2];

        Map<String, String> headers = parseHeaders(reader);

        byte[] bodyContent = null;
        if (requireReadBody(headers)) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            bodyContent = IOUtils.readData(reader, contentLength).getBytes();
        }

        return new HttpRequest(method, path, version, headers, parseQueryParameters(pathToken), bodyContent);
    }

    private static boolean requireReadBody(Map<String, String> headers) {
        return headers.containsKey("Content-Length");
    }

    private Map<String, String> parseHeaders(BufferedReader reader) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String header;
        while ((header = reader.readLine()) != null) {
            if (header.isEmpty()) {
                break;
            }
            String[] headerTokens = header.split(":", 2);
            headers.put(headerTokens[0].trim(), headerTokens[1].trim());
        }
        return headers;
    }

    private Map<String, String> parseQueryParameters(String pathToken) {
        String[] token = pathToken.split("\\?");
        if (token.length < 2) {
            return Collections.emptyMap();
        }

        Map<String, String> queryParameters = new HashMap<>();
        String[] queries =  token[1].split("&");

        for (String query: queries) {
            String[] queryTokens = query.split("=");
            String key = queryTokens[0];
            String value = queryTokens[1];
            queryParameters.put(key, value);
        }
        return queryParameters;
    }
}
