package org.primshic.stepan.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.primshic.stepan.model.Location;
import org.primshic.stepan.model.User;
import org.primshic.stepan.util.HibernateUtil;

import javax.persistence.Query;
import java.util.List;

public class LocationRepository {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public List<Location> getUserLocations(User user) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Location WHERE user = :user";
            return session.createQuery(hql, Location.class)
                    .setParameter("user", user)
                    .getResultList();
        }
    }

    public void add(Location location) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.persist(location);
            session.getTransaction().commit();
        }
    }

    public void delete(int userId, double lat, double lon) {
        try(Session session = sessionFactory.openSession()) {
            String jpql = "DELETE FROM Location l WHERE l.user.id = :userId AND l.lat = :lat AND l.lon = :lon";

            Query query = session.createQuery(jpql);
            query.setParameter("userId", userId);
            query.setParameter("lat", lat);
            query.setParameter("lon", lon);
            query.executeUpdate();
        }
    }
}
