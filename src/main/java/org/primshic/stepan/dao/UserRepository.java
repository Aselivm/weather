package org.primshic.stepan.dao;

import org.hibernate.SessionFactory;
import org.primshic.stepan.model.User;
import org.primshic.stepan.util.HibernateUtil;

import java.util.Optional;

public class UserRepository {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    public Optional<User> persist(User user) {
        try(org.hibernate.Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }
        return Optional.of(user);
    }

    public Optional<User> get(String login, String password) {
        try(org.hibernate.Session session = sessionFactory.openSession()){
            return Optional.ofNullable(session.createQuery
                            ("FROM User WHERE login = :login AND password = :password", User.class)
                    .setParameter("login", login)
                    .setParameter("password",password)
                    .uniqueResult());
        }
    }
}
