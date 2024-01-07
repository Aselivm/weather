package org.primshic.stepan.filter;

import org.hibernate.SessionFactory;
import org.primshic.stepan.auth.session.Session;
import org.primshic.stepan.auth.session.SessionService;
import org.primshic.stepan.util.HibernateUtil;
import org.primshic.stepan.util.SessionUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@WebFilter(urlPatterns = "/*")
public class Authentication implements Filter {

    private SessionService sessionService;

    private SessionFactory sessionFactory;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        sessionFactory = HibernateUtil.getSessionFactory();
        sessionService = new SessionService(sessionFactory);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Optional<Session> userSession = SessionUtil.getSessionFromCookies(request,sessionService);
        request.setAttribute("userSession",userSession);
        request.setAttribute("sessionFactory",sessionFactory);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
