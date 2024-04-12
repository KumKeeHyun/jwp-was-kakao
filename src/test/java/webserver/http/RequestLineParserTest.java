package webserver.http;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;

class RequestLineParserTest {

    @Test
    void RequestLine을_파싱한다() throws IOException {
        // given
        String requestLineMessage = "GET /pub/WWW/TheProject.html?jebal=tonggua HTTP/1.1";
        InputStream in = new ByteArrayInputStream(requestLineMessage.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        // when
        RequestLine requestLine = RequestLineParser.parse(reader);

        // then
        assertThat(requestLine.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(requestLine.getPath()).isEqualTo("/pub/WWW/TheProject.html");
        assertThat(requestLine.getVersion()).isEqualTo("HTTP/1.1");
        assertThat(requestLine.getQueryParameter("jebal")).isEqualTo("tonggua");
    }

}