package org.primshic.stepan.controller;

import org.primshic.stepan.services.SessionService;
import org.primshic.stepan.util.CookieUtil;
import org.primshic.stepan.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/log-out")
public class DeleteServlet extends HttpServlet {
    private SessionService sessionService;

    @Override
    public void init() throws ServletException {
        sessionService = (SessionService) getServletConfig().getServletContext().getAttribute("sessionService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = CookieUtil.getSessionIdByCookie(req.getCookies());
        SessionUtil.deleteSessionIfPresent(sessionId,sessionService);
        resp.sendRedirect(req.getContextPath()+"/auth");
    }
}
