package webserver.mvc;

import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StaticRenderer {

    private final List<StaticView> views;

    public StaticRenderer(String ... bases) {
        this.views = Arrays.stream(bases)
                .map(StaticView::new)
                .collect(Collectors.toList());
    }

    public void render(HttpResponse response, String path) {
        for (StaticView view : views) {
            try {
                response.responseBody(view.render(path));
                response.contentType(extractMediaType(path));
                response.responseStatus(HttpStatus.OK);
                return;
            } catch (Exception ignore) {}
        }
        response.responseStatus(HttpStatus.NOT_FOUND);
        response.contentType(MediaType.HTML);
        response.responseBody((path + "를 찾을 수 없습니다.").getBytes());
    }

    private MediaType extractMediaType(String path) {
        int index = path.lastIndexOf(".");
        if (index == -1) {
            return MediaType.OCTET_STREAM;
        }
        String extension = path.substring(index + 1).toLowerCase();
        return MediaType.fromExtension(extension);
    }

}
