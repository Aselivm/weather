package org.primshic.stepan.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.primshic.stepan.model.Location;
import org.primshic.stepan.model.User;
import java.util.List;

public class LocationRepository {

    private SessionFactory sessionFactory;
    public LocationRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
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

    public void delete(int databaseId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            String hql = "DELETE FROM Location WHERE id = :databaseId";
            session.createQuery(hql).setParameter("databaseId", databaseId).executeUpdate();
            session.getTransaction().commit();
        }
    }
}
