package webserver.request;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpHeadersParserTest {

    @Test
    void Http_Request의_Headers를_파싱한다() throws IOException {
        // given
        String headersMessage = "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*\n" +
                "\n";
        InputStream in = new ByteArrayInputStream(headersMessage.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        // when
        HttpHeaders headers = HttpHeadersParser.parse(reader);

        // then
        assertThat(headers.size()).isEqualTo(3);
        assertThat(headers.getFieldValue("Host")).isEqualTo("localhost:8080");
        assertThat(headers.getFieldValue("Connection")).isEqualTo("keep-alive");
        assertThat(headers.getFieldValue("Accept")).isEqualTo("*/*");
    }

}
