package org.primshic.stepan.dao;

import org.hibernate.SessionFactory;
import org.primshic.stepan.model.Session;
import org.primshic.stepan.model.User;
import org.primshic.stepan.util.HibernateUtil;

import java.util.Optional;

public class SessionRepository {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    public void save(User user) {
        try(org.hibernate.Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }
    }

    public Optional<Session> getByUserId(int id) {
        try(org.hibernate.Session session = sessionFactory.openSession()){
            return Optional.ofNullable(session.createQuery("FROM Session WHERE user.id = :userId", Session.class)
                    .setParameter("userId", id)
                    .uniqueResult());
        }
    }

    public Optional<Session> getById(String uuid) {
        try(org.hibernate.Session session = sessionFactory.openSession()){
            return  Optional.of(session.get(Session.class,uuid));
        }
    }
}
