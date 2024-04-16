package webserver.mvc;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.util.function.Predicate;

public class SessionLoginMiddlewareUtil {

    public static Handler redirectIfAlreadyLogin(Handler innerHandler, String redirectUrl, String sessionName) {
        return redirect(
                innerHandler,
                redirectUrl,
                request -> SessionLoginMiddlewareUtil.isAlreadyLogin(request, sessionName)
        );
    }

    private static Handler redirect(Handler innerHandler, String redirectUrl, Predicate<HttpRequest> predicate) {
        return request -> {
            if (predicate.test(request)) {
                HttpResponse response = new HttpResponse();
                response.redirectUrl(redirectUrl);
                return response;
            }
            return innerHandler.handle(request);
        };
    }

    private static boolean isAlreadyLogin(HttpRequest request, String sessionName) {
        return (request.getSession(sessionName)) != null;
    }

    public static Handler redirectIfNotLogin(Handler innerHandler, String redirectUrl, String sessionName) {
        return redirect(
                innerHandler,
                redirectUrl,
                request -> SessionLoginMiddlewareUtil.isNotLogin(request, sessionName)
        );
    }


    private static boolean isNotLogin(HttpRequest request, String sessionName) {
        return (request.getSession(sessionName)) == null;
    }

}
