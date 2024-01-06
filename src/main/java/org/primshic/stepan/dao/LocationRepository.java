package org.primshic.stepan.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.primshic.stepan.model.Location;
import org.primshic.stepan.model.User;
import org.primshic.stepan.util.HibernateUtil;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

public class LocationRepository {
    private Logger log = Logger.getLogger(LocationRepository.class.getName());
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

    public void delete(int userId, BigDecimal lat, BigDecimal lon) {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String jpql = "DELETE FROM Location l WHERE l.user.id = :userId " +
                    "AND ROUND(l.lat, 4) = ROUND(:lat, 4) " +
                    "AND ROUND(l.lon, 4) = ROUND(:lon, 4)";

            Query query = session.createQuery(jpql);
            log.info("user id: "+userId);
            log.info("lat: "+lat);
            log.info("lon: "+lon);
            query.setParameter("userId", userId);
            query.setParameter("lat", lat);
            query.setParameter("lon", lon);
            query.executeUpdate();
            session.getTransaction().commit();
        }
    }
}
