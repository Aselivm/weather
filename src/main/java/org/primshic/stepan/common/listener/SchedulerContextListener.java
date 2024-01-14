package org.primshic.stepan.common.listener;

import lombok.extern.slf4j.Slf4j;
import org.primshic.stepan.auth.session.SessionRepository;
import org.primshic.stepan.auth.session.SessionService;
import org.primshic.stepan.common.util.HibernateUtil;
import org.primshic.stepan.common.util.PropertyReaderUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
@Slf4j
public class SchedulerContextListener implements ServletContextListener {

    private static final long init = Long.parseLong(PropertyReaderUtil.read("scheduler.properties","initialDelay"));
    private static final long period = Long.parseLong(PropertyReaderUtil.read("scheduler.properties","period"));

    private ScheduledExecutorService executorService;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            log.info("Initializing SchedulerContextListener...");

            executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.scheduleAtFixedRate(
                    new SessionService(HibernateUtil.getSessionFactory())::deleteExpiredSessions,
                    init,
                    period,
                    TimeUnit.MINUTES
            );

            log.info("SchedulerContextListener initialized successfully.");
        } catch (Exception e) {
            log.error("Failed to initialize SchedulerContextListener", e);
            throw new RuntimeException("Failed to initialize SchedulerContextListener", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        executorService.shutdown();
        log.info("SchedulerContextListener destroyed.");
    }

}
