package org.primshic.stepan.dao;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.primshic.stepan.model.Session;
import org.primshic.stepan.model.User;
import org.primshic.stepan.util.HibernateUtil;

import java.util.Optional;

public class SessionRepository {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    public void save(Session sessionEntity) {
        try (org.hibernate.Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(sessionEntity);
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
            return  Optional.ofNullable(session.get(Session.class,uuid));
        }
    }

    public void deleteExpiredSessions() {
        try(org.hibernate.Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.createQuery("delete from Session where expiresAt<CURRENT_TIMESTAMP ").executeUpdate();
            session.getTransaction().commit();
        }catch (RuntimeException e){
            e.printStackTrace();
        }
    }
}
