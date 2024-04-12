package webserver.mvc.route;

import org.junit.jupiter.api.Test;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.mvc.Handler;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FakeHandler implements Handler {

    private final String id;

    public FakeHandler(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws IOException {
        return null;
    }
}

class RouteTreeTest {

    @Test
    void static_path로_핸들러를_등록할_수_있다() {
        // given
        RouteTree root = new RouteTree(TreeNodeType.ROOT, "", "", null);
        root.addRoute("", new FakeHandler("0"));
        root.addRoute("/", new FakeHandler("1"));
        root.addRoute("/a", new FakeHandler("2"));
        root.addRoute("/b", new FakeHandler("3"));
        root.addRoute("/c/d", new FakeHandler("4"));
        root.addRoute("/e/f/", new FakeHandler("5"));

        // when-then
        RouteValue rv = root.findRoute("").orElseThrow();
        assertThat(((FakeHandler)rv.getHandler()).getId()).isEqualTo("0");

        rv = root.findRoute("/").orElseThrow();
        assertThat(((FakeHandler)rv.getHandler()).getId()).isEqualTo("1");

        rv = root.findRoute("/a").orElseThrow();
        assertThat(((FakeHandler)rv.getHandler()).getId()).isEqualTo("2");

        rv = root.findRoute("/b").orElseThrow();
        assertThat(((FakeHandler)rv.getHandler()).getId()).isEqualTo("3");

        rv = root.findRoute("/c/d").orElseThrow();
        assertThat(((FakeHandler)rv.getHandler()).getId()).isEqualTo("4");

        rv = root.findRoute("/e/f/").orElseThrow();
        assertThat(((FakeHandler)rv.getHandler()).getId()).isEqualTo("5");

        assertThatThrownBy(() -> root.findRoute("/c").orElseThrow());
        assertThatThrownBy(() -> root.findRoute("/c/d/").orElseThrow());
        assertThatThrownBy(() -> root.findRoute("/e/f").orElseThrow());
    }

    @Test
    void param_path로_핸들러를_등록할_수_있다() {
        // given
        RouteTree root = new RouteTree(TreeNodeType.ROOT, "", "", null);
        root.addRoute("/a/:key1", new FakeHandler("1"));
        root.addRoute("/a/:key1/b", new FakeHandler("2"));
        root.addRoute("/a/:key1/b/:key2", new FakeHandler("3"));

        // when-then
        RouteValue rv = root.findRoute("/a/foo1").orElseThrow();
        assertThat(((FakeHandler)rv.getHandler()).getId()).isEqualTo("1");
        assertThat(rv.getPathVariables().get("key1")).isEqualTo("foo1");

        rv = root.findRoute("/a/foo2").orElseThrow();
        assertThat(((FakeHandler)rv.getHandler()).getId()).isEqualTo("1");
        assertThat(rv.getPathVariables().get("key1")).isEqualTo("foo2");

        rv = root.findRoute("/a/foo1/b").orElseThrow();
        assertThat(((FakeHandler)rv.getHandler()).getId()).isEqualTo("2");
        assertThat(rv.getPathVariables().get("key1")).isEqualTo("foo1");

        rv = root.findRoute("/a/foo2/b").orElseThrow();
        assertThat(((FakeHandler)rv.getHandler()).getId()).isEqualTo("2");
        assertThat(rv.getPathVariables().get("key1")).isEqualTo("foo2");

        rv = root.findRoute("/a/foo1/b/bar1").orElseThrow();
        assertThat(((FakeHandler)rv.getHandler()).getId()).isEqualTo("3");
        assertThat(rv.getPathVariables().get("key1")).isEqualTo("foo1");
        assertThat(rv.getPathVariables().get("key2")).isEqualTo("bar1");

        rv = root.findRoute("/a/foo2/b/bar2").orElseThrow();
        assertThat(((FakeHandler)rv.getHandler()).getId()).isEqualTo("3");
        assertThat(rv.getPathVariables().get("key1")).isEqualTo("foo2");
        assertThat(rv.getPathVariables().get("key2")).isEqualTo("bar2");

    }

    @Test
    void catch_all_path로_핸들러를_등록할_수_있다() {
        // given
        RouteTree root = new RouteTree(TreeNodeType.ROOT, "", "", null);
        root.addRoute("/a/*", new FakeHandler("1"));
        root.addRoute("/b/*", new FakeHandler("2"));

        // when-then
        RouteValue rv = root.findRoute("/a/").orElseThrow();
        assertThat(((FakeHandler)rv.getHandler()).getId()).isEqualTo("1");

        rv = root.findRoute("/a/b").orElseThrow();
        assertThat(((FakeHandler)rv.getHandler()).getId()).isEqualTo("1");

        rv = root.findRoute("/a/b/c").orElseThrow();
        assertThat(((FakeHandler)rv.getHandler()).getId()).isEqualTo("1");

        rv = root.findRoute("/b/").orElseThrow();
        assertThat(((FakeHandler)rv.getHandler()).getId()).isEqualTo("2");

        rv = root.findRoute("/b/c").orElseThrow();
        assertThat(((FakeHandler)rv.getHandler()).getId()).isEqualTo("2");

        rv = root.findRoute("/b/c/d").orElseThrow();
        assertThat(((FakeHandler)rv.getHandler()).getId()).isEqualTo("2");
    }

    @Test
    void catch_all과_param를_혼용해서_핸들러를_등록할_수_있다() {
        // given
        RouteTree root = new RouteTree(TreeNodeType.ROOT, "", "", null);
        root.addRoute("/a/:key/b/*", new FakeHandler("1"));

        // when
        RouteValue rv = root.findRoute("/a/foo1/b/").orElseThrow();
        assertThat(((FakeHandler)rv.getHandler()).getId()).isEqualTo("1");
        assertThat(rv.getPathVariables().get("key")).isEqualTo("foo1");

        rv = root.findRoute("/a/foo2/b/c/d").orElseThrow();
        assertThat(((FakeHandler)rv.getHandler()).getId()).isEqualTo("1");
        assertThat(rv.getPathVariables().get("key")).isEqualTo("foo2");

        rv = root.findRoute("/a/foo3/b/c/d/e").orElseThrow();
        assertThat(((FakeHandler)rv.getHandler()).getId()).isEqualTo("1");
        assertThat(rv.getPathVariables().get("key")).isEqualTo("foo3");
    }

    @Test
    void path는_충돌될_수_없다() {
        // given
        RouteTree root = new RouteTree(TreeNodeType.ROOT, "", "", null);
        root.addRoute("/a/:key", new FakeHandler("1"));
        root.addRoute("/b/c", new FakeHandler("2"));

        // when
        assertThatThrownBy(() -> root.addRoute("/a/b", new FakeHandler("3")));
        assertThatThrownBy(() -> root.addRoute("/*", new FakeHandler("4")));
        assertThatThrownBy(() -> root.addRoute("/b/c", new FakeHandler("5")));
    }

}