package org.primshic.stepan.services;

import org.primshic.stepan.dao.SessionRepository;
import org.primshic.stepan.model.Session;
import org.primshic.stepan.model.User;

import java.util.Optional;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SessionService {

    public SessionService() {
        sessionCollector();
    }

    private boolean collectorStarted = false;
    private final SessionRepository sessionRepository = new SessionRepository();
    public Optional<Session> startSession(User user) {
        Session session = new Session();
        session.setUser(user);
        return sessionRepository.startSession(session);
    }

    public Optional<Session> getById(String uuid) {
        return sessionRepository.getById(uuid);
    }

    public void sessionCollector(){
        if(!collectorStarted){
            ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
            scheduledThreadPoolExecutor.scheduleWithFixedDelay(sessionRepository::deleteExpiredSessions, 1, 1, TimeUnit.HOURS);
            collectorStarted = true;
        }
    }

    public void delete(Session session){
        sessionRepository.delete(session);
    }
}
