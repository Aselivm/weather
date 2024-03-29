package org.primshic.stepan.web.auth.session;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;;
import org.primshic.stepan.web.auth.user.User;

import java.util.Optional;

@Slf4j
public class SessionService {
    private final SessionRepository sessionRepository;
    public SessionService(SessionFactory sessionFactory) {
        sessionRepository = new SessionRepository(sessionFactory);
    }
    public Optional<Session> startSession(User user) {
        log.info("Starting a new session for user: {}", user.getLogin());
        Session session = new Session();
        session.setUser(user);
        Optional<Session> startedSession = sessionRepository.startSession(session);
        log.info("Session started: {}", startedSession.orElse(null));
        return startedSession;
    }

    public void deleteExpiredSessions() {
        sessionRepository.deleteExpiredSessions();
    }

    public Optional<Session> getById(String uuid) {
        log.info("Getting session by ID: {}", uuid);
        Optional<Session> session = sessionRepository.getById(uuid);
        log.info("Retrieved session: {}", session.orElse(null));
        return session;
    }



    public void delete(Session session) {
        log.info("Deleting session: {}", session);
        sessionRepository.delete(session);
        log.info("Session deleted");
    }
}
