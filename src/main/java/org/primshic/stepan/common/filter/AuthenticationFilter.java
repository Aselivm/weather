package org.primshic.stepan.common.filter;

import org.hibernate.SessionFactory;
import org.primshic.stepan.auth.session.Session;
import org.primshic.stepan.auth.session.SessionService;
import org.primshic.stepan.common.util.HibernateUtil;
import org.primshic.stepan.common.util.SessionUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@WebFilter(urlPatterns = "/*")
public class AuthenticationFilter implements Filter {

    private SessionService sessionService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        sessionService = new SessionService(sessionFactory);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Optional<Session> userSession = SessionUtil.getSessionFromCookies(request,sessionService);
        request.getSession().setAttribute("userSession",userSession);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
