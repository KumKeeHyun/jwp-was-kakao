package webserver.response;

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

    public static MediaType fromExtension(String extension) {
        extension = extension.toLowerCase();
        for (MediaType mediaType : values()) {
            if (mediaType.extension.equals(extension)) {
                return mediaType;
            }
        }
        return OCTET_STREAM;
    }

    public String getMediaType() {
        return mediaType + ";charset=utf-8";
    }

}
