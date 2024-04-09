package webserver.handler;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class URIMatcherTest {

    @ParameterizedTest
    @ValueSource(strings = { "/static/", "/static/a", "/static/a/b", "/static/abc/*", "/static/**" })
    void 와일드카드_두칸을_매칭한다(String path) {
        // give
        String uri = "/static/**";

        // when
        boolean result = URIMatcher.match(uri, path);

        // then
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = { "/static/", "/static/a" })
    void 와일드카드를_매칭한다(String path) {
        // given
        String uri = "/static/*";

        // when
        boolean result = URIMatcher.match(uri, path);

        // then
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = { "/static/a/b", "/static", "/static/a/b/c" })
    void 와일드카드를_매칭이_안돼야한다(String path) {
        // given
        String uri = "/static/*";

        // when
        boolean result = URIMatcher.match(uri, path);

        // then
        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = { "/api/a", "/api/abc" })
    void pathVariable을_매칭한다(String path) {
        // given
        String uri = "/api/{var}";

        // when
        boolean result = URIMatcher.match(uri, path);

        // then
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = { "/api/a/b", "/api/abc/c" })
    void pathVariable_매칭이_안돼야한다(String path) {
        // given
        String uri = "/api/{var}";

        // when
        boolean result = URIMatcher.match(uri, path);

        // then
        assertThat(result).isFalse();
    }
}
