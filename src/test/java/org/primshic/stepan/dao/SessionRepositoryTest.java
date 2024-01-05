package org.primshic.stepan.dao;

import org.hibernate.SessionFactory;
import org.primshic.stepan.model.TestSession;
import org.primshic.stepan.util.TestHibernateUtil;

import java.util.Optional;

public class SessionRepositoryTest {
    private final SessionFactory sessionFactory = TestHibernateUtil.getSessionFactory();
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
            return  Optional.of(session.get(TestSession.class,uuid));
        }
    }
}
