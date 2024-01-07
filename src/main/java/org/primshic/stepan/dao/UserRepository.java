package org.primshic.stepan.dao;

import org.primshic.stepan.model.User;

import java.util.Optional;

public class UserRepository extends BaseRepository {
    public Optional<User> persist(User user) {
        try(org.hibernate.Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }
        return Optional.of(user);
    }

    public Optional<User> get(String login) {
        try(org.hibernate.Session session = sessionFactory.openSession()){
            return Optional.ofNullable(session.createQuery
                            ("FROM User WHERE login = :login", User.class)
                    .setParameter("login", login)
                    .uniqueResult());
        }
    }
}
