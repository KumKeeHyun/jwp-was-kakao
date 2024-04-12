package webserver.http;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum MediaType {
    HTML("text/html", "html"),
    CSS("text/css", "css"),
    JAVASCRIPT("text/javascript", "js"),
    JSON("application/json", "json"),
    OCTET_STREAM("application/octet-stream", "");

    private final String mediaType;
    private final String extension;

    MediaType(String mediaType, String extension) {
        this.mediaType = mediaType;
        this.extension = extension;
    }

    private static final Map<String, MediaType> mediaTypes;

    static {
        mediaTypes = Arrays.stream(values())
                .collect(Collectors.toUnmodifiableMap(MediaType::getExtension, Function.identity()));
    }

    public static MediaType fromExtension(String extension) {
        extension = extension.toLowerCase();
        return mediaTypes.getOrDefault(extension, OCTET_STREAM);
    }

    private String getExtension() {
        return extension;
    }

    public String getMediaType() {
        return mediaType + ";charset=utf-8";
    }

}
