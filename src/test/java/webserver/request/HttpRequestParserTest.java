package webserver.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

public class HttpRequestParserTest {

    @Test
    void RequestLine을_파싱한다() throws IOException {
        // given
        String requestMessage = "GET / HTTP/1.1\n";
        InputStream in = new ByteArrayInputStream(requestMessage.getBytes());

        // when
        HttpRequestParser parser = new HttpRequestParser();
        HttpRequest request = parser.parseRequest(in);

        // then
        assertAll(
                () -> assertThat(request.getMethod()).isEqualTo(HttpMethod.GET),
                () -> assertThat(request.getPath()).isEqualTo("/"),
                () -> assertThat(request.getVersion()).isEqualTo("HTTP/1.1")
        );
    }

    @Test
    void 헤더를_파싱한다() throws IOException {
        // given
        String requestMessage = "GET / HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*\n" +
                "\n";
        InputStream in = new ByteArrayInputStream(requestMessage.getBytes());

        // when
        HttpRequestParser parser = new HttpRequestParser();
        HttpRequest request = parser.parseRequest(in);

        // then
        assertAll(
                () -> assertThat(request.getHeader("Host")).isEqualTo("localhost:8080"),
                () -> assertThat(request.getHeader("Connection")).isEqualTo("keep-alive"),
                () -> assertThat(request.getHeader("Accept")).isEqualTo("*/*")
        );
    }

    @Test
    void 쿼리_파라매터를_파싱한다() throws IOException {
        // given
        String requestMessage = "GET /?a=b&c=d HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*\n" +
                "\n";
        InputStream in = new ByteArrayInputStream(requestMessage.getBytes());

        // when
        HttpRequestParser parser = new HttpRequestParser();
        HttpRequest request = parser.parseRequest(in);

        // then
        assertAll(
                () -> assertThat(request.getPath()).isEqualTo("/"),
                () -> assertThat(request.getParameter("a")).isEqualTo("b"),
                () -> assertThat(request.getParameter("c")).isEqualTo("d")
        );
    }

    @Test
    void 바디를_파싱한다() throws IOException {
        // given
        String body = "body\nbodyb";
        String requestMessage = "POST /?a=b&c=d HTTP/1.1\n" +
                "Content-Length: " + body.getBytes().length + "\n" +
                "\n"
                + body;
        InputStream in = new ByteArrayInputStream(requestMessage.getBytes());

        // when
        HttpRequestParser parser = new HttpRequestParser();
        HttpRequest request = parser.parseRequest(in);

        // then
        assertThat(request.getBodyContent()).isEqualTo(body.getBytes());
    }
}
