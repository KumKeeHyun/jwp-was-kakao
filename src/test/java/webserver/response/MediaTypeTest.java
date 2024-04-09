package webserver.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;

public class MediaTypeTest {

    @Test
    void 확장자를_MediaType으로_변환한다() {
        // given & when & then
        assertAll(
                () -> assertThat(MediaType.fromExtension("html")).isEqualTo(MediaType.HTML),
                () -> assertThat(MediaType.fromExtension("css")).isEqualTo(MediaType.CSS),
                () -> assertThat(MediaType.fromExtension("js")).isEqualTo(MediaType.JAVASCRIPT),
                () -> assertThat(MediaType.fromExtension("invalid")).isEqualTo(MediaType.OCTET_STREAM),
                () -> assertThat(MediaType.fromExtension("")).isEqualTo(MediaType.OCTET_STREAM)
        );
    }
}
