package webserver.mvc.route;

import webserver.mvc.Handler;

import java.util.Map;

public class RouteValue {

    private final Handler handler;
    private final Map<String, String> pathVariables;

    public RouteValue(Handler handler, Map<String, String> pathVariables) {
        this.handler = handler;
        this.pathVariables = pathVariables;
    }

    public Handler getHandler() {
        return handler;
    }

    public Map<String, String> getPathVariables() {
        return pathVariables;
    }

}
