package webserver;

import webserver.http.Session;

public interface SessionManager {

    void add(Session session);

    Session findSession(String id);

    void remove(String id);
}
