package org.primshic.stepan.repo_mock.dao;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.primshic.stepan.repo_mock.model.TestSession;
import org.primshic.stepan.repo_mock.util.TestHibernateUtil;

import java.util.Optional;
import java.util.logging.Logger;

public class SessionRepositoryTest {
    private Logger log = Logger.getLogger(SessionRepositoryTest.class.getName());

    private final SessionFactory sessionFactory;
    public SessionRepositoryTest(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    public void save(TestSession sessionEntity) {
        try (org.hibernate.Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(sessionEntity);
            session.getTransaction().commit();
        }
    }

    public Optional<TestSession> getByUserId(int id) {
        try(org.hibernate.Session session = sessionFactory.openSession()){
            return Optional.ofNullable(session.createQuery("FROM TestSession WHERE user.id = :userId", TestSession.class)
                    .setParameter("userId", id)
                    .uniqueResult());
        }
    }

    public Optional<TestSession> getById(String uuid) {
        try(org.hibernate.Session session = sessionFactory.openSession()){
            return  Optional.ofNullable(session.get(TestSession.class,uuid));
        }
    }

    public void deleteExpiredSessions() {
        try(org.hibernate.Session session = sessionFactory.openSession()){
            session.beginTransaction();
            int deletedRows = session.createQuery("delete from TestSession where expiresAt<CURRENT_TIMESTAMP ").executeUpdate();
            session.getTransaction().commit();
            log.info("Number of deleted rows: "+deletedRows);
        }catch (RuntimeException e){
            e.printStackTrace();
        }
    }
}
