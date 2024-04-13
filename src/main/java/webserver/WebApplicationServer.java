package webserver;

import api.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequestParser;
import webserver.http.HttpResponse;
import webserver.http.HttpResponseRenderer;
import webserver.mvc.Handler;
import webserver.mvc.StaticRenderer;
import webserver.mvc.StaticResourceHandler;
import webserver.mvc.TemplateRenderer;
import webserver.mvc.route.RouteHandler;

import java.net.ServerSocket;
import java.net.Socket;

public class WebApplicationServer {
    private static final Logger logger = LoggerFactory.getLogger(WebApplicationServer.class);
    private static final int DEFAULT_PORT = 8080;
    private static final HttpRequestParser requestParser = new HttpRequestParser();
    private static final HttpResponseRenderer responseRenderer = new HttpResponseRenderer();
    private static final Handler handler;

    static {
        StaticResourceHandler staticResourceHandler = new StaticResourceHandler(new StaticRenderer("./templates", "./static"));
        TemplateRenderer templateRenderer = new TemplateRenderer("/templates");

        RouteHandler route = new RouteHandler(staticResourceHandler);
        route.addGet("/", request -> {
            HttpResponse response = new HttpResponse();
            response.redirectUrl("/index.html");
            return response;
        });
        UserController userController = new UserController(templateRenderer);
        route.addPost("/user/create", userController::createUser);
        route.addGet("/user/list", userController::listUser);

        handler = route;
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);

            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                Thread thread = new Thread(new RequestHandler(requestParser, responseRenderer, connection, handler));
                thread.start();
            }
        }
    }

}
