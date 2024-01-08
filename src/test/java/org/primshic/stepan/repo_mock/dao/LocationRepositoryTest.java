package org.primshic.stepan.repo_mock.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.primshic.stepan.repo_mock.model.TestLocation;
import org.primshic.stepan.repo_mock.util.TestHibernateUtil;

import javax.persistence.Query;

public class LocationRepositoryTest {
    private final SessionFactory sessionFactory;
    public LocationRepositoryTest(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void add(TestLocation location) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.persist(location);
            session.getTransaction().commit();
        }
    }

    public void delete(int databaseId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            String hql = "DELETE FROM TestLocation WHERE id = :databaseId";
            session.createQuery(hql).setParameter("databaseId", databaseId).executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
