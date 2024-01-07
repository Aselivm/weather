package org.primshic.stepan.services;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.primshic.stepan.dao.SessionRepository;
import org.primshic.stepan.model.Session;
import org.primshic.stepan.model.User;

import java.util.Optional;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
