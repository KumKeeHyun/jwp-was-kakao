package webserver.mvc;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public class StaticResourceHandler implements Handler {

    private final StaticRenderer renderer;

    public StaticResourceHandler(StaticRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        String path = request.getPath();
        HttpResponse response = new HttpResponse();
        renderer.render(response, path);
        return response;
    }

}
