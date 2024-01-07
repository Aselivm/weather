package org.primshic.stepan.listener;

import org.hibernate.SessionFactory;
import org.primshic.stepan.weather.locations.LocationService;
import org.primshic.stepan.auth.session.SessionService;
import org.primshic.stepan.auth.user.UserService;
import org.primshic.stepan.util.HibernateUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class RepositoryContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        LocationService locationService = new LocationService(sessionFactory);
        UserService userService = new UserService(sessionFactory);
        SessionService sessionService = new SessionService(sessionFactory);

        servletContextEvent.getServletContext().setAttribute("locationService",locationService);
        servletContextEvent.getServletContext().setAttribute("userService",userService);
        servletContextEvent.getServletContext().setAttribute("sessionService",sessionService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        HibernateUtil.shutdown();
    }
}
