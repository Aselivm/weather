package org.primshic.stepan.listener;

import org.primshic.stepan.dao.SessionRepository;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class DeleteExpiredSessionAutomation implements ServletContextListener {
    private ScheduledExecutorService executorService;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new SessionRepository()::deleteExpiredSessions, 60, 30, TimeUnit.MINUTES);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        executorService.shutdown();
    }

}
