package org.primshic.stepan.repo_mock;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.primshic.stepan.util.HibernateUtil;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestHibernateUtil {
    private static volatile StandardServiceRegistry registry;
    private static volatile SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory(){
        if(sessionFactory==null){
            synchronized (HibernateUtil.class){
                try {
                    registry = new StandardServiceRegistryBuilder()
                            .configure("testHibernate.cfg.xml")
                            .build();
                    MetadataSources sources = new MetadataSources(registry);
                    Metadata metadata = sources.getMetadataBuilder().build();
                    sessionFactory = metadata.getSessionFactoryBuilder().build();
                }catch (Exception e) {
                    e.printStackTrace();
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
        }
    }
}
