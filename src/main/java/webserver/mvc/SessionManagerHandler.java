package webserver.mvc;

import webserver.SessionManager;
import webserver.http.HttpCookie;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.Session;

import java.io.IOException;

public class SessionManagerHandler implements Handler {

    public static final String SESSION_COOKIE = "JSESSIONID";
    private final Handler innerHandler;
    private final SessionManager sessionManager;

    public SessionManagerHandler(Handler innerHandler, SessionManager sessionManager) {
        this.innerHandler = innerHandler;
        this.sessionManager = sessionManager;
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws IOException {
        preHandle(request);
        HttpResponse response = innerHandler.handle(request);
        return postHandle(response);
    }

    private void preHandle(HttpRequest request) {
        HttpCookie sessionCookie = request.getCookie(SESSION_COOKIE);
        if (sessionCookie != null) {
            String sessionKey = sessionCookie.getValue();
            request.withSession(
                    () -> sessionManager.findSession(sessionKey),
                    () -> sessionManager.remove(sessionKey)
            );
        }
    }

    private HttpResponse postHandle(HttpResponse response) {
        Session newSession = response.getSession();
        if (newSession != null) {
            sessionManager.add(newSession);
            HttpCookie newSessionCookie = new HttpCookie(SESSION_COOKIE, newSession.getId());
            newSessionCookie.setPath("/");
            response.setCookie(newSessionCookie);
        }
        return response;
    }
}
