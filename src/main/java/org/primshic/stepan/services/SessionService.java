package org.primshic.stepan.services;

import org.primshic.stepan.dao.SessionRepository;
import org.primshic.stepan.model.Session;
import org.primshic.stepan.model.User;

import javax.servlet.http.Cookie;
import java.util.Optional;

public class SessionService {
    private final SessionRepository sessionRepository = new SessionRepository();
    public Optional<Session> startSession(User user) {
        Session session = new Session();
        session.setUser(user);
        sessionRepository.save(session);
        return sessionRepository.getByUserId(user.getId());
    }

    public Optional<Session> getById(String uuid) {
        return sessionRepository.getById(uuid);
    }
}
