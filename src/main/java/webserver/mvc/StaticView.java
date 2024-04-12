package webserver.mvc;

import utils.FileIoUtils;

public class StaticView {

    private final String basePath;

    public StaticView(String basePath) {
        this.basePath = basePath;
    }


    public StaticView() {
        this("");
    }

    public byte[] render(String path) throws Exception {
        return FileIoUtils.loadFileFromClasspath(basePath + path);
    }

}
