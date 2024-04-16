package webserver.http;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestTest {

    @Test
    void 여러_세션을_전달할_수_있다() {
        // given
        HttpRequest request = new HttpRequest(HttpMethod.GET, "/session/test", "HTTP/1.1",
                Collections.emptyMap(), null);

        // when
        request.withSession("1", () -> new Session("1-id"), () -> {});
        request.withSession("2", () -> new Session("2-id"), () -> {});

        // then
        assertThat(request.getSession("1")).isNotNull().extracting(Session::getId).isEqualTo("1-id");
        assertThat(request.getSession("2")).isNotNull().extracting(Session::getId).isEqualTo("2-id");
        assertThat(request.getSession("3")).isNull();
    }

    @Test
    void 세션을_삭제할_수_있다() {
        // given
        HttpRequest request = new HttpRequest(HttpMethod.GET, "/session/test", "HTTP/1.1",
                Collections.emptyMap(), null);
        Map<String, Session> store = new HashMap<>();
        store.put("1", new Session("1-id"));
        store.put("2", new Session("2-id"));
        request.withSession("1", () -> store.get("1"), () -> store.remove("1"));
        request.withSession("2", () -> store.get("2"), () -> store.remove("2"));

        // when
        request.removeSession("1");

        // then
        assertThat(request.getSession("1")).isNull();
        assertThat(request.getSession("2")).isNotNull().extracting(Session::getId).isEqualTo("2-id");
    }

}