package org.primshic.stepan.web.servlets.account;

import org.primshic.stepan.web.auth.session.Session;
import org.primshic.stepan.web.auth.session.SessionService;
import org.primshic.stepan.util.SessionUtil;
import org.primshic.stepan.web.servlets.WeatherTrackerBaseServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = "/log-out")
public class LogOutServlet extends WeatherTrackerBaseServlet {
    private SessionService sessionService;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        sessionService = (SessionService) servletContext.getAttribute("sessionService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<Session> optionalUserSession = SessionUtil.getSessionByReq(req);
        SessionUtil.deleteSessionIfPresent(optionalUserSession,sessionService);
        resp.sendRedirect(req.getContextPath()+"/auth");
    }
}
