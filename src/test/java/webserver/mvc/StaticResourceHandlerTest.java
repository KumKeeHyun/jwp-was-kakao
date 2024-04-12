package webserver.mvc;

import org.junit.jupiter.api.Test;
import utils.FileIoUtils;
import webserver.http.HttpMethod;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class StaticResourceHandlerTest {

    @Test
    void static_파일을_응답한다() throws IOException, URISyntaxException {
        // given
        StaticResourceHandler handler = new StaticResourceHandler(new StaticRenderer("./templates", "./static"));
        HttpRequest request = new HttpRequest(HttpMethod.GET, "/static-test.css", "HTTP/1.1",
                Collections.emptyMap(), null);
        byte[] expected = FileIoUtils.loadFileFromClasspath("./static/static-test.css");

        // when
        HttpResponse response = handler.handle(request);

        // then
        assertAll(
                () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getContent()).isEqualTo(expected)
        );
    }

    @Test
    void templates_파일을_응답한다() throws IOException, URISyntaxException {
        // given
        StaticResourceHandler handler = new StaticResourceHandler(new StaticRenderer("./templates", "./static"));
        HttpRequest request = new HttpRequest(HttpMethod.GET, "/templates-test.css", "HTTP/1.1",
                Collections.emptyMap(), null);
        byte[] expected = FileIoUtils.loadFileFromClasspath("./templates/templates-test.css");

        // when
        HttpResponse response = handler.handle(request);

        // then
        assertAll(
                () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getContent()).isEqualTo(expected)
        );
    }

    @Test
    void 둘다_있으면_templates_파일을_응답한다() throws IOException, URISyntaxException {
        // given
        StaticResourceHandler handler = new StaticResourceHandler(new StaticRenderer("./templates", "./static"));
        HttpRequest request = new HttpRequest(HttpMethod.GET, "/test.html", "HTTP/1.1",
                Collections.emptyMap(), null);
        byte[] expected = FileIoUtils.loadFileFromClasspath("./templates/test.html");

        // when
        HttpResponse response = handler.handle(request);

        // then
        assertAll(
                () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getContent()).isEqualTo(expected)
        );
    }

    @Test
    void 파일이_존재하지_않으면_404_응답을_반환한다() {
        // given
        StaticResourceHandler handler = new StaticResourceHandler(new StaticRenderer("./templates", "./static"));
        HttpRequest request = new HttpRequest(HttpMethod.GET, "/invalid.html", "HTTP/1.1",
                Collections.emptyMap(), null);

        // when
        HttpResponse response = handler.handle(request);

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
