package webserver.mvc;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.util.function.Predicate;

public class SessionMiddlewareUtil {

    public static Handler redirectIfAlreadyLogin(Handler innerHandler, String redirectUrl) {
        return redirect(innerHandler, redirectUrl, SessionMiddlewareUtil::isAlreadyLogin);
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

    private static boolean isAlreadyLogin(HttpRequest request) {
        return (request.getSession()) != null;
    }

    public static Handler redirectIfNotLogin(Handler innerHandler, String redirectUrl) {
        return redirect(innerHandler, redirectUrl, SessionMiddlewareUtil::isNotLogin);
    }


    private static boolean isNotLogin(HttpRequest request) {
        return (request.getSession()) == null;
    }

}
