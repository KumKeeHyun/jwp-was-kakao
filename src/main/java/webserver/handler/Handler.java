package webserver.handler;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.io.IOException;

@FunctionalInterface
public interface Handler {

    HttpResponse handle(HttpRequest request) throws IOException;
}
