package org.primshic.stepan.controller;

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
    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = CookieUtil.getSessionIdByCookie(req.getCookies());
        SessionUtil.deleteSessionIfPresent(sessionId);
        resp.sendRedirect(req.getContextPath()+"/auth");
    }
}
