package org.primshic.stepan.repo_mock.dao;

import org.hibernate.SessionFactory;
import org.primshic.stepan.repo_mock.model.TestUser;
import org.primshic.stepan.repo_mock.util.TestHibernateUtil;

import java.util.Optional;

public class UserRepositoryTest {
    private final SessionFactory sessionFactory = TestHibernateUtil.getSessionFactory();
    public Optional<TestUser> persist(TestUser user) {
        try(org.hibernate.Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }
        return Optional.of(user);
    }

    public Optional<TestUser> get(String login) {
        try(org.hibernate.Session session = sessionFactory.openSession()){
            return Optional.ofNullable(session.createQuery
                            ("FROM TestUser WHERE login = :login", TestUser.class)
                    .setParameter("login", login)
                    .uniqueResult());
        }
    }
}
