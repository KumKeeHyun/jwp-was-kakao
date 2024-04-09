package webserver.handler;

import java.io.IOException;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

@FunctionalInterface
public interface Handler {

    HttpResponse handle(HttpRequest request) throws IOException;
}
