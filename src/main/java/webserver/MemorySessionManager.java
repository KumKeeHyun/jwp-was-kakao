package webserver;

import webserver.http.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemorySessionManager implements SessionManager {

    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @Override
    public void add(Session session) {
        sessions.put(session.getId(), session);
    }

    @Override
    public Session findSession(String id) {
        return sessions.get(id);
    }

    @Override
    public void remove(String id) {
        sessions.remove(id);
    }

}
