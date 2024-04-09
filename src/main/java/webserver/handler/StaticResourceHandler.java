package webserver.handler;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import webserver.request.HttpRequest;
import webserver.response.FileView;
import webserver.response.HttpResponse;
import webserver.response.HttpStatus;
import webserver.response.MediaType;

public class StaticResourceHandler implements Handler {

    private final List<FileView> views;

    public StaticResourceHandler(String... bases) {
        this.views = Arrays.stream(bases)
                           .map(FileView::new)
                           .collect(Collectors.toList());
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        String path = request.getPath();

        return views.stream()
                    .map(view -> view.readFromPath(path))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(content -> okResponse(path, content))
                    .findFirst()
                    .orElseGet(this::notFoundResponse);
    }

    private HttpResponse okResponse(String path, byte[] content) {
        HttpResponse response = new HttpResponse();
        response.responseStatus(HttpStatus.OK);
        response.contentType(extractMediaType(path));
        response.responseBody(content);
        return response;
    }

    private MediaType extractMediaType(String path) {
        int index = path.lastIndexOf(".");
        if (index == -1) {
            return MediaType.OCTET_STREAM;
        }
        String extension = path.substring(index + 1).toLowerCase();
        return MediaType.fromExtension(extension);
    }

    private HttpResponse notFoundResponse() {
        HttpResponse response = new HttpResponse();
        response.responseStatus(HttpStatus.NOT_FOUND);
        return response;
    }
}
