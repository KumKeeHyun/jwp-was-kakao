package webserver.mvc;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.http.MediaType;

public class TemplateRenderer {

    private static final String SUFFIX = ".html";
    private final Handlebars handlebars;

    public TemplateRenderer(String basePath) {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix(basePath);
        loader.setSuffix(SUFFIX);
        handlebars = new Handlebars(loader);
    }

    public void render(HttpResponse response, String path, Object model) {
        try {
            byte[] content = handlebars.compile(path).apply(model).getBytes();
            response.responseBody(content);
            response.contentType(MediaType.HTML);
            response.responseStatus(HttpStatus.OK);
            return;
        } catch (Exception ignore) {}

        response.responseStatus(HttpStatus.NOT_FOUND);
        response.contentType(MediaType.HTML);
        response.responseBody((path + "를 찾을 수 없습니다.").getBytes());
    }
}
