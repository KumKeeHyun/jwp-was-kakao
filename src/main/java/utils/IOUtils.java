package utils;

import java.io.BufferedReader;
import java.io.IOException;

public class IOUtils {
    /**
     * @param br 는
     *            Request Body를 시작하는 시점이어야
     * @param contentLength 는
     *            Request Header의 Content-Length 값이다.
     * @return Request Body를 String으로 변환하여 반환한다.
     */
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        int readLength = br.read(body, 0, contentLength);
        if (readLength != contentLength) {
            throw new RuntimeException("Content-Length와 body의 길이가 다릅니다.");
        }
        return String.copyValueOf(body);
    }
}
