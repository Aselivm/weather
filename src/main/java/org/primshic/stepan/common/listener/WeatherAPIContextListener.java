package org.primshic.stepan.common.listener;

import org.primshic.stepan.weather.openWeatherAPI.WeatherAPIService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class WeatherAPIContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce){
        WeatherAPIService weatherAPIService = new WeatherAPIService();
        sce.getServletContext().setAttribute("weatherAPIService",weatherAPIService);
    }

}
