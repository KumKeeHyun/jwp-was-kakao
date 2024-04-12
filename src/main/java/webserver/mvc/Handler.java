package webserver.mvc;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.IOException;

@FunctionalInterface
public interface Handler {

    HttpResponse handle(HttpRequest request) throws IOException;
}
