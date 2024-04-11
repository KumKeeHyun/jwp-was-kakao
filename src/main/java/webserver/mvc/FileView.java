package webserver.mvc;

import utils.FileIoUtils;

import java.util.Optional;

public class FileView {

    private final String basePath;

    public FileView(String basePath) {
        this.basePath = basePath;
    }


    public FileView() {
        this("");
    }

    public Optional<byte[]> readFromPath(String path) {
        try {
            return Optional.of(FileIoUtils.loadFileFromClasspath(basePath + path));
        } catch (Exception ignored) {
        }
        return Optional.empty();
    }

}
