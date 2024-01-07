package org.primshic.stepan.listener;

import org.primshic.stepan.auth.session.SessionRepository;
import org.primshic.stepan.util.HibernateUtil;
import org.primshic.stepan.util.PropertyReaderUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class SchedulerContextListener implements ServletContextListener {

    private static final long init = Long.parseLong(PropertyReaderUtil.read("scheduler.properties","initialDelay"));
    private static final long period = Long.parseLong(PropertyReaderUtil.read("scheduler.properties","period"));

    private ScheduledExecutorService executorService;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate
                (new SessionRepository
                        (HibernateUtil.getSessionFactory())
                        ::deleteExpiredSessions, init, period, TimeUnit.MINUTES);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        executorService.shutdown();
    }

}
