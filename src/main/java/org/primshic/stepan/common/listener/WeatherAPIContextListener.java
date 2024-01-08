package org.primshic.stepan.common.listener;

import lombok.extern.slf4j.Slf4j;
import org.primshic.stepan.weather.openWeatherAPI.WeatherAPIService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
@Slf4j
public class WeatherAPIContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            log.info("Initializing WeatherAPIContextListener...");

            WeatherAPIService weatherAPIService = new WeatherAPIService();
            sce.getServletContext().setAttribute("weatherAPIService", weatherAPIService);

            log.info("WeatherAPIContextListener initialized successfully.");
        } catch (Exception e) {
            log.error("Failed to initialize WeatherAPIContextListener", e);
            throw new RuntimeException("Failed to initialize WeatherAPIContextListener", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // В случае необходимости выполните какие-либо действия при уничтожении контекста
    }
}
