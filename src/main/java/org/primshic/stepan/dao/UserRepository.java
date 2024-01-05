package org.primshic.stepan.dao;

import org.hibernate.SessionFactory;
import org.primshic.stepan.model.User;
import org.primshic.stepan.util.HibernateUtil;

import java.util.Optional;

public class UserRepository {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    public Optional<User> persist(User user) {
        return null;
    }

    public Optional<User> get(String login, String password) {
        return null;
    }
}
