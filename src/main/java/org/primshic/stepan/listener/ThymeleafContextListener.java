package org.primshic.stepan.listener;

import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
@Slf4j
public class ThymeleafContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            log.info("Initializing ThymeleafContextListener...");

            ServletContext servletContext = sce.getServletContext();
            TemplateEngine templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(thymeleafTemplateResolver(servletContext));

            servletContext.setAttribute("templateEngine", templateEngine);

            log.info("ThymeleafContextListener initialized successfully.");
        } catch (Exception e) {
            log.error("Failed to initialize ThymeleafContextListener", e);
            throw new RuntimeException("Failed to initialize ThymeleafContextListener", e);
        }
    }

    private ServletContextTemplateResolver thymeleafTemplateResolver(ServletContext servletContext) {
        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver(servletContext);
        resolver.setPrefix("WEB-INF/view/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }
}
