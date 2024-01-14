package org.primshic.stepan.weather.locations;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.primshic.stepan.auth.user.User;
import org.primshic.stepan.common.exception.ApplicationException;
import org.primshic.stepan.common.exception.ErrorMessage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
public class LocationRepository {

    private final SessionFactory sessionFactory;

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

            String hql = "DELETE FROM Location WHERE user.id = :userId AND ROUND(lat, 4) = :latitude AND ROUND(lon, 4) = :longitude";
            session.createQuery(hql)
                    .setParameter("userId", userId)
                    .setParameter("latitude", lat.setScale(4, RoundingMode.HALF_UP))
                    .setParameter("longitude", lon.setScale(4, RoundingMode.HALF_UP))
                    .executeUpdate();

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
