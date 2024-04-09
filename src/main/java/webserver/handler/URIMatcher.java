package webserver.handler;

public final class URIMatcher {

    // TODO: 리펙토링
    public static boolean match(String uri, String path) {
        if (uri.equals(path)) {
            return true;
        }

        if (uri.endsWith("/**")) {
            return path.startsWith(uri.substring(0, uri.length() - 2));
        }

        if (uri.endsWith("/*")) {
            return path.startsWith(uri.substring(0, uri.length() - 1)) && path.indexOf("/", uri.length() - 1) == -1;
        }

        if (uri.contains("{")) {
            String[] uriTokens = uri.split("/");
            String[] pathTokens = path.split("/");

            if (uriTokens.length != pathTokens.length) {
                return false;
            }

            for (int i = 0; i < uriTokens.length; i++) {
                if (uriTokens[i].startsWith("{") && uriTokens[i].endsWith("}")) {
                    continue;
                }

                if (!uriTokens[i].equals(pathTokens[i])) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }
}
