package org.primshic.stepan.weather.locations;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.primshic.stepan.web.auth.user.User;
import org.primshic.stepan.exception.ApplicationException;
import org.primshic.stepan.exception.ErrorMessage;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
public class LocationRepository {

    private final SessionFactory sessionFactory;

    private static final double EPSILON = 0.01;

    public LocationRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Location> getUserLocations(User user) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Location WHERE user = :user";
            return session.createQuery(hql, Location.class)
                    .setParameter("user", user)
                    .getResultList();
        } catch (HibernateException e) {
            log.error("Error while fetching user locations", e);
            throw new ApplicationException(ErrorMessage.DATABASE_ERROR);
        }
    }

    public void add(Location location) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(location);
            transaction.commit();
        } catch (HibernateException e){
            log.error("Error while adding location", e);
            if (transaction != null && transaction.isActive()) {
                   transaction.rollback();
            }
            throw new ApplicationException(ErrorMessage.DATABASE_ERROR);
        }
    }

    public void delete(int userId, BigDecimal lat, BigDecimal lon) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            String hqlSelect = "FROM Location WHERE user.id = :userId " +
                    "AND lat BETWEEN :minLat AND :maxLat " +
                    "AND lon BETWEEN :minLon AND :maxLon " +
                    "ORDER BY ABS(lat - :latitude) + ABS(lon - :longitude)";
            Location locationToDelete = (Location) session.createQuery(hqlSelect)
                    .setParameter("userId", userId)
                    .setParameter("minLat", lat.subtract(new BigDecimal(EPSILON)))
                    .setParameter("maxLat", lat.add(new BigDecimal(EPSILON)))
                    .setParameter("minLon", lon.subtract(new BigDecimal(EPSILON)))
                    .setParameter("maxLon", lon.add(new BigDecimal(EPSILON)))
                    .setParameter("latitude", lat)
                    .setParameter("longitude", lon)
                    .setMaxResults(1)
                    .uniqueResult();

            if (locationToDelete != null) {
                session.delete(locationToDelete);
            }
            transaction.commit();
        } catch (HibernateException e) {
            log.error("Error while deleting location", e);
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new ApplicationException(ErrorMessage.DATABASE_ERROR);
        }
    }
}
