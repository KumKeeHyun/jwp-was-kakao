package webserver;

import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import api.UserController;
import webserver.handler.Handler;
import webserver.handler.HandlerComposite;
import webserver.request.HttpMethod;
import webserver.request.HttpRequestParser;
import webserver.response.HttpResponseRenderer;

public class WebApplicationServer {
    private static final Logger logger = LoggerFactory.getLogger(WebApplicationServer.class);
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws Exception {
        int port;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }
        Handler handler = routeHandler();
        HttpRequestParser parser = new HttpRequestParser();
        HttpResponseRenderer renderer = new HttpResponseRenderer();

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);

            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                Thread thread = new Thread(new RequestHandler(parser, renderer, connection, handler));
                thread.start();
            }
        }
    }

    // TODO: 리펙토링
    private static HandlerComposite routeHandler() {
        HandlerComposite composite = new HandlerComposite();
        UserController userController = new UserController();
        composite.addHandler(HttpMethod.POST, "/user/create", userController::createUser);
        return composite;
    }
}
