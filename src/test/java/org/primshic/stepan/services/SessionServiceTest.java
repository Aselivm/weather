package org.primshic.stepan.services;

import org.primshic.stepan.dao.SessionRepositoryTest;
import org.primshic.stepan.model.TestSession;
import org.primshic.stepan.model.TestUser;

import java.util.Optional;

public class SessionServiceTest {
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
}
