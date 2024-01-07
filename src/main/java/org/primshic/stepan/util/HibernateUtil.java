package org.primshic.stepan.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HibernateUtil {
    private static volatile StandardServiceRegistry registry;
    private static volatile SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            synchronized (HibernateUtil.class) {
                try {
                    registry = new StandardServiceRegistryBuilder()
                            .configure("hibernate.cfg.xml")
                            .build();
                    MetadataSources sources = new MetadataSources(registry);
                    Metadata metadata = sources.getMetadataBuilder().build();
                    sessionFactory = metadata.getSessionFactoryBuilder().build();
                    log.info("Hibernate SessionFactory initialized successfully.");
                } catch (Exception e) {
                    log.error("Error initializing Hibernate SessionFactory.", e);
                    if (registry != null) {
                        StandardServiceRegistryBuilder.destroy(registry);
                    }
                }
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
            log.info("Hibernate SessionFactory shutdown.");
        }
    }
}
