package org.primshic.stepan.dao;

import org.hibernate.SessionFactory;
import org.primshic.stepan.model.Session;
import org.primshic.stepan.model.User;
import org.primshic.stepan.services.UserService;
import org.primshic.stepan.util.HibernateUtil;

import java.util.Optional;

public class SessionRepository {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    public void save(User user) {
    }

    public Optional<Session> getByUserId(int id) {
        return null;
    }

    public Optional<Session> getById(String uuid) {
        return null;
    }
}
