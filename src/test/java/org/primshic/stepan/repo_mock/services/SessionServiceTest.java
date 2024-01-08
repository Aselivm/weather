package org.primshic.stepan.repo_mock.services;

import org.hibernate.SessionFactory;
import org.primshic.stepan.repo_mock.dao.LocationRepositoryTest;
import org.primshic.stepan.repo_mock.dao.SessionRepositoryTest;
import org.primshic.stepan.repo_mock.model.TestSession;
import org.primshic.stepan.repo_mock.model.TestUser;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SessionServiceTest {
    private final SessionRepositoryTest sessionRepository;

    public SessionServiceTest(SessionFactory sessionFactory) {
        sessionRepository = new SessionRepositoryTest(sessionFactory);
    }
    public Optional<TestSession> startSession(TestUser testUser) {
        TestSession session = new TestSession();
        session.setUser(testUser);
        sessionRepository.save(session);
        return sessionRepository.getByUserId(testUser.getId());
    }

    public Optional<TestSession> getById(String uuid) {
        return sessionRepository.getById(uuid);
    }

}
