package org.primshic.stepan.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.*;

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

                    executeMigrationScripts(sessionFactory);
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


    private static void executeMigrationScripts(SessionFactory sessionFactory) {
        executeSqlScript(sessionFactory, "drop_table_sessions.sql");
        executeSqlScript(sessionFactory, "drop_table_locations.sql");
        executeSqlScript(sessionFactory, "drop_table_users.sql");
        executeSqlScript(sessionFactory, "create_users_table.sql");
        executeSqlScript(sessionFactory, "create_sessions_table.sql");
        executeSqlScript(sessionFactory, "create_locations_table.sql");
        executeSqlScript(sessionFactory, "login_index.sql");
        executeSqlScript(sessionFactory, "expiresAt_index.sql");
    }

    private static void executeSqlScript(SessionFactory sessionFactory, String scriptFileName) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                StringBuilder sqlScript = readSqlScript(scriptFileName);

                session.createNativeQuery(sqlScript.toString()).executeUpdate();

                transaction.commit();
            } catch (IOException e) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
    }

    private static StringBuilder readSqlScript(String scriptFileName) throws IOException {
        StringBuilder sqlScript = new StringBuilder();

        try (InputStream inputStream = TestHibernateUtil.class.getClassLoader().getResourceAsStream("sql_scripts/"+scriptFileName)) {
            assert inputStream != null;
            try (InputStreamReader streamReader = new InputStreamReader(inputStream);
                 BufferedReader reader = new BufferedReader(streamReader)) {

                String line;
                while ((line = reader.readLine()) != null) {
                    sqlScript.append(line).append("\n");
                }
            }
        }

        return sqlScript;
    }

}
