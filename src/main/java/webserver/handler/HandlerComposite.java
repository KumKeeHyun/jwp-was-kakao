package webserver.handler;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import webserver.request.HttpMethod;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public class HandlerComposite implements Handler {

    private final Map<HttpMethod, URIMapping> handlers;

    public HandlerComposite() {
        this.handlers = new EnumMap<>(HttpMethod.class);
        this.handlers.put(HttpMethod.GET, new URIMapping());
        this.handlers.put(HttpMethod.POST, new URIMapping());
    }

    public void addHandler(HttpMethod method, String uri, Handler handler) {
        if (handlers.containsKey(method)) {
            handlers.get(method).addHandler(uri, handler);
        } else {
            URIMapping uriMapping = new URIMapping();
            uriMapping.addHandler(uri, handler);
            handlers.put(method, uriMapping);
        }
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws IOException {
        URIMapping handler = findURIMapping(request.getMethod());
        return handler.findHandler(request.getPath()).handle(request);
    }

    private URIMapping findURIMapping(HttpMethod method) {
        URIMapping handler = handlers.get(method);
        if (handler == null) {
            throw new IllegalArgumentException("해당하는 핸들러가 없습니다.");
        }
        return handler;
    }
}
