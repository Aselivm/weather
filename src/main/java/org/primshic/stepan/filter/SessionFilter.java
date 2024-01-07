package org.primshic.stepan.filter;

import org.primshic.stepan.model.Session;
import org.primshic.stepan.util.SessionUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@WebFilter("/*")
public class SessionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        Optional<Session> optionalUserSession = SessionUtil.getSessionByCookieReq(httpRequest);
        httpRequest.setAttribute("userSession", optionalUserSession.orElse(null));
        chain.doFilter(request, response);
    }
}
