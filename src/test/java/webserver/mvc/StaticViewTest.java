package webserver.mvc;

import org.junit.jupiter.api.Test;
import utils.FileIoUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StaticViewTest {

    @Test
    void 파일을_읽어온다() throws Exception {
        // given
        StaticView view = new StaticView();
        byte[] expected = FileIoUtils.loadFileFromClasspath("./test.html");

        // when
        byte[] actual = view.render("./test.html");

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 파일이_존재하지_않으면_예외를_던진다() {
        // given
        StaticView view = new StaticView();

        // when-then
        assertThatThrownBy(() -> view.render("./invalid.html"));

    }
}
