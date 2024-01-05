package org.primshic.stepan.repo_mock.services;

import org.primshic.stepan.repo_mock.dao.SessionRepositoryTest;
import org.primshic.stepan.repo_mock.model.TestSession;
import org.primshic.stepan.repo_mock.model.TestUser;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SessionServiceTest {
    public SessionServiceTest() {
        sessionCollector();
    }

    private boolean collectorStarted = false;
    private final SessionRepositoryTest sessionRepository = new SessionRepositoryTest();
    public Optional<TestSession> startSession(TestUser testUser) {
        TestSession session = new TestSession();
        session.setUser(testUser);
        sessionRepository.save(session);
        return sessionRepository.getByUserId(testUser.getId());
    }

    public Optional<TestSession> getById(String uuid) {
        return sessionRepository.getById(uuid);
    }

    public void sessionCollector(){
        if(!collectorStarted){
            ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
            scheduledThreadPoolExecutor.scheduleWithFixedDelay(sessionRepository::deleteExpiredSessions, 6, 5, TimeUnit.SECONDS);
            collectorStarted = true;
        }
    }
}
