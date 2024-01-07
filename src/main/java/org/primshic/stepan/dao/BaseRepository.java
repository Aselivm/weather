package org.primshic.stepan.dao;

import org.hibernate.SessionFactory;
import org.primshic.stepan.util.HibernateUtil;

public abstract class BaseRepository {
    protected final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
}
