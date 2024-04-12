package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequest;
import webserver.http.HttpRequestParser;
import webserver.http.HttpResponse;
import webserver.http.HttpResponseRenderer;
import webserver.mvc.Handler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final HttpRequestParser parser;
    private final HttpResponseRenderer renderer;
    private final Socket connection;
    private final Handler handler;

    public RequestHandler(HttpRequestParser parser, HttpResponseRenderer renderer, Socket connectionSocket, Handler handler) {
        this.parser = parser;
        this.renderer = renderer;
        this.connection = connectionSocket;
        this.handler = handler;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                     connection.getPort());

        try (InputStream in = connection.getInputStream(); DataOutputStream out = new DataOutputStream(connection.getOutputStream())) {
            HttpRequest request = parser.parseRequest(in);
            HttpResponse response = handler.handle(request);
            renderer.render(out, response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
