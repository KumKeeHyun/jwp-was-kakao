package webserver.mvc.route;

public enum TreeNodeType {
    ROOT,
    STATIC,
    PARAM,
    CATCH_ALL;

    public static TreeNodeType of(String path) {
        if (path.startsWith("/:")) {
            return PARAM;
        }
        if (path.startsWith("/*")) {
            return CATCH_ALL;
        }
        return STATIC;
    }
}
