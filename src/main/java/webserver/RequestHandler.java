package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.handler.Handler;
import webserver.request.HttpRequest;
import webserver.request.HttpRequestParser;
import webserver.response.HttpResponse;
import webserver.response.HttpResponseRenderer;

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

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = parser.parseRequest(in);
            DataOutputStream dos = new DataOutputStream(out);
            HttpResponse response = handler.handle(request);
            renderer.render(dos, response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
