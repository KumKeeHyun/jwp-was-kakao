package webserver.mvc.route;

import webserver.http.HttpMethod;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.MediaType;
import webserver.mvc.Handler;

import java.io.IOException;
import java.util.Collections;
import java.util.EnumMap;

public class RouteHandler implements Handler {

    private final EnumMap<HttpMethod, RouteTree> table = new EnumMap<>(HttpMethod.class);
    private final RouteTree emptyRouteTree = new RouteTree(TreeNodeType.ROOT, "", "", null);
    private final RouteValue defaultRoute;

    public RouteHandler() {
        this(request -> {
            HttpResponse response = new HttpResponse();
            response.notFound((request.getPath() + "를 찾을 수 없습니다.").getBytes());
            response.contentType(MediaType.HTML);
            return response;
        });
    }

    public RouteHandler(Handler defaultRoute) {
        this.defaultRoute = new RouteValue(defaultRoute, Collections.emptyMap());
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws IOException {
        RouteValue routeValue = table.getOrDefault(request.getMethod(), emptyRouteTree)
                .findRoute(request.getPath())
                .orElse(defaultRoute);
        request.setPathVariables(routeValue.getPathVariables());
        return routeValue.getHandler().handle(request);
    }

    private void addRoute(HttpMethod method, String path, Handler handler) {
        table.computeIfAbsent(method, methodKey -> new RouteTree(TreeNodeType.ROOT, "", "", null));
        table.get(method).addRoute(path, handler);
    }

    public void addGet(String path, Handler handler) {
        addRoute(HttpMethod.GET, path, handler);
    }

    public void addPost(String path, Handler handler) {
        addRoute(HttpMethod.POST, path, handler);
    }

}
