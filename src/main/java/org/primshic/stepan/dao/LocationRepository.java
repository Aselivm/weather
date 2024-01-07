package org.primshic.stepan.dao;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.primshic.stepan.model.Location;
import org.primshic.stepan.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            throw e;
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
            throw e;
        }
    }

    public void delete(int databaseId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            String hql = "DELETE FROM Location WHERE id = :databaseId";
            session.createQuery(hql).setParameter("databaseId", databaseId).executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            log.error("Error while deleting location", e);
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
