package webserver.mvc;

import webserver.http.HttpCookie;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.Session;

import java.io.IOException;

public class SessionManagerHandler implements Handler {

    private final Handler innerHandler;
    private final String sessionName;
    private final SessionManager sessionManager;


    public SessionManagerHandler(String sessionName, SessionManager sessionManager, Handler innerHandler) {
        this.sessionName = sessionName;
        this.sessionManager = sessionManager;
        this.innerHandler = innerHandler;
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws IOException {
        preHandle(request);
        HttpResponse response = innerHandler.handle(request);
        return postHandle(response);
    }

    private void preHandle(HttpRequest request) {
        HttpCookie sessionCookie = request.getCookie(sessionName);
        if (sessionCookie != null) {
            String sessionKey = sessionCookie.getValue();
            request.withSession(
                    sessionName,
                    () -> sessionManager.findSession(sessionKey),
                    () -> sessionManager.remove(sessionKey)
            );
        }
    }

    private HttpResponse postHandle(HttpResponse response) {
        Session newSession = response.getSession(sessionName);
        if (newSession != null) {
            sessionManager.add(newSession);
            HttpCookie newSessionCookie = new HttpCookie(sessionName, newSession.getId());
            newSessionCookie.setPath("/");
            response.addCookie(newSessionCookie);
        }
        return response;
    }
}
