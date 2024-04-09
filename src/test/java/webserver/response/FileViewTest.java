package webserver.response;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import utils.FileIoUtils;

public class FileViewTest {

    @Test
    void HttpResponse를_생성한다() throws IOException, URISyntaxException {
        // given
        FileView view = new FileView();
        byte[] expected = FileIoUtils.loadFileFromClasspath("./test.html");

        // when
        Optional<byte[]> actual = view.readFromPath("./test.html");

        // then
        assertThat(actual).hasValue(expected);
    }

    @Test
    void 파일이_존재하지_않으면_empty를_반환한다() {
        // given
        FileView view = new FileView();

        // when
        Optional<byte[]> actual = view.readFromPath("./invalid.html");

        // then
        assertThat(actual).isEmpty();
    }
}
