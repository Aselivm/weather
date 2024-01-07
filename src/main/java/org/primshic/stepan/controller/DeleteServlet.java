package org.primshic.stepan.controller;

import org.primshic.stepan.model.Session;
import org.primshic.stepan.services.SessionService;
import org.primshic.stepan.util.CookieUtil;
import org.primshic.stepan.util.SessionUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = "/log-out")
public class DeleteServlet extends HttpServlet {
    private Optional<Session> optionalUserSession;
    private SessionService sessionService;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        sessionService = (SessionService) servletContext.getAttribute("sessionService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        optionalUserSession = SessionUtil.getSessionByReq(req);
        SessionUtil.deleteSessionIfPresent(optionalUserSession,sessionService);
        resp.sendRedirect(req.getContextPath()+"/auth");
    }
}
