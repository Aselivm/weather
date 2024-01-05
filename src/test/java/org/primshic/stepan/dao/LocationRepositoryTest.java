package org.primshic.stepan.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.primshic.stepan.model.TestLocation;
import org.primshic.stepan.util.TestHibernateUtil;

import javax.persistence.Query;

public class LocationRepositoryTest {
    private final SessionFactory sessionFactory = TestHibernateUtil.getSessionFactory();

    public void add(TestLocation location) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.persist(location);
            session.getTransaction().commit();
        }
    }

    public void delete(int userId, double lat, double lon) {
        try(Session session = sessionFactory.openSession()) {
            String jpql = "DELETE FROM TestLocation l WHERE l.user.id = :userId AND l.lat = :lat AND l.lon = :lon";

            Query query = session.createQuery(jpql);
            query.setParameter("userId", userId);
            query.setParameter("lat", lat);
            query.setParameter("lon", lon);
            query.executeUpdate();
        }
    }
}
