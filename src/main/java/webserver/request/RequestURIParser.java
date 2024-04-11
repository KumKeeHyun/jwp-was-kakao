package webserver.request;

import java.util.HashMap;
import java.util.Map;

public class RequestURIParser {

    public static final String QUERY_CHAR = "?";
    public static final String QUERY_DELIMITER = "&";
    public static final String KEY_VALUE_DELIMITER = "=";

    /*
    parse = "*" | absoluteURI | abs_path
    */
    public static RequestURI parse(String rawURI) {
        if (isAsterisk(rawURI)) {
            return new RequestURI("", "", "*", new HashMap<>());
        }

        String scheme = extractScheme(rawURI);
        String uriWithoutScheme = removeScheme(rawURI, scheme);

        String host = extractHost(uriWithoutScheme);
        String pathWithQuery = extractPathWithQuery(uriWithoutScheme);

        String path = extractPath(pathWithQuery);
        Map<String, String> queryParameters = extractQueryParameters(pathWithQuery);

        return new RequestURI(scheme, host, path, queryParameters);
    }

    private static boolean isAsterisk(String uri) {
        return "*".equals(uri);
    }

    private static String extractScheme(String uri) {
        int schemeEnd = uri.indexOf("://");
        if (schemeEnd != -1) {
            return uri.substring(0, schemeEnd);
        }
        return "";
    }

    private static String removeScheme(String uri, String scheme) {
        if (!scheme.isEmpty()) {
            return uri.substring(scheme.length() + 3);
        }
        return uri;
    }

    private static String extractHost(String uri) {
        int pathStart = uri.indexOf("/");
        if (pathStart != -1) {
            return uri.substring(0, pathStart);
        }
        return "";
    }

    private static String extractPathWithQuery(String uri) {
        int pathStart = uri.indexOf("/");
        if (pathStart != -1) {
            return uri.substring(pathStart);
        }
        return "/";
    }

    private static String extractPath(String pathWithQuery) {
        int queryStart = pathWithQuery.indexOf(QUERY_CHAR);
        if (queryStart != -1) {
            return pathWithQuery.substring(0, queryStart);
        }
        return pathWithQuery;
    }

    private static Map<String, String> extractQueryParameters(String pathWithQuery) {
        Map<String, String> queryParameters = new HashMap<>();
        int queryStart = pathWithQuery.indexOf(QUERY_CHAR);
        if (queryStart != -1) {
            String[] queryParams = pathWithQuery.substring(queryStart + 1).split(QUERY_DELIMITER);
            for (String param : queryParams) {
                String[] keyValue = param.split(KEY_VALUE_DELIMITER, 2);
                if (keyValue.length == 2) {
                    queryParameters.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return queryParameters;
    }
}
