package webserver.http;

import org.junit.jupiter.api.Test;
import webserver.http.RequestURI;
import webserver.http.RequestURIParser;

import static org.assertj.core.api.Assertions.assertThat;

class RequestURIParserTest {

    @Test
    void asterisk를_파싱한다() {
        // given
        String uriMessage = "*";

        // when
        RequestURI uri = RequestURIParser.parse(uriMessage);

        // then
        assertThat(uri.getScheme()).isEqualTo("");
        assertThat(uri.getHost()).isEqualTo("");
        assertThat(uri.getPath()).isEqualTo("*");
    }

    @Test
    void absoluteURI를_파싱한다() {
        // given
        String uriMessage = "http://www.w3.org/pub/WWW/TheProject.html";

        // when
        RequestURI uri = RequestURIParser.parse(uriMessage);

        // then
        assertThat(uri.getScheme()).isEqualTo("http");
        assertThat(uri.getHost()).isEqualTo("www.w3.org");
        assertThat(uri.getPath()).isEqualTo("/pub/WWW/TheProject.html");
    }

    @Test
    void abs_path를_파싱한다() {
        // given
        String uriMessage = "/pub/WWW/TheProject.html";

        // when
        RequestURI uri = RequestURIParser.parse(uriMessage);

        // then
        assertThat(uri.getScheme()).isEqualTo("");
        assertThat(uri.getHost()).isEqualTo("");
        assertThat(uri.getPath()).isEqualTo("/pub/WWW/TheProject.html");
    }

    @Test
    void query를_파싱한다() {
        // given
        String uriMessage = "/pub/WWW/TheProject.html?name=glen.kum&prev=duman.kum&test=a=b";

        // when
        RequestURI uri = RequestURIParser.parse(uriMessage);

        // then
        assertThat(uri.getScheme()).isEqualTo("");
        assertThat(uri.getHost()).isEqualTo("");
        assertThat(uri.getPath()).isEqualTo("/pub/WWW/TheProject.html");
        assertThat(uri.getQueryParameters().get("name")).isEqualTo("glen.kum");
        assertThat(uri.getQueryParameters().get("prev")).isEqualTo("duman.kum");
        assertThat(uri.getQueryParameters().get("test")).isEqualTo("a=b");
    }
}