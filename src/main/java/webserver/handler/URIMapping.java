package webserver.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class URIMapping {

    private final Map<String, Handler> handlers;

    private final Handler defaultHandler;

    public URIMapping() {
        this.handlers = new HashMap<>();
        this.defaultHandler = new StaticResourceHandler("./templates", "./static");
    }

    public void addHandler(String uri, Handler handler) {
        handlers.entrySet()
                .stream()
                .filter(entry -> URIMatcher.match((entry.getKey()), uri))
                .findAny()
                .ifPresent(entry -> {
                    throw new IllegalArgumentException("중복된 URI가 존재합니다.");
                });
        handlers.put(uri, handler);
    }

    public Handler findHandler(String path) {
        return handlers.entrySet()
                       .stream()
                       .filter((entry) -> URIMatcher.match(entry.getKey(), path))
                       .map(Entry::getValue)
                       .findFirst()
                       .orElse(defaultHandler);
    }
}
