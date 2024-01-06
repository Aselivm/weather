package org.primshic.stepan.controller;

import org.checkerframework.checker.units.qual.A;
import org.primshic.stepan.dto.account.UserDTO;
import org.primshic.stepan.exception.ApplicationException;
import org.primshic.stepan.exception.ErrorMessage;
import org.primshic.stepan.exception.ExceptionHandler;
import org.primshic.stepan.model.Session;
import org.primshic.stepan.model.User;
import org.primshic.stepan.util.CookieUtil;
import org.primshic.stepan.util.InputUtil;
import org.primshic.stepan.util.SessionUtil;
import org.primshic.stepan.util.ThymeleafUtil;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/reg")
public class Registration extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            String sessionId = CookieUtil.getSessionIdByCookie(req.getCookies());
            SessionUtil.deleteSessionIfPresent(sessionId);
            ThymeleafUtil.templateEngineProcess("registration", req,resp);
        }catch (ApplicationException e){
            ExceptionHandler.handle(resp,e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UserDTO userDTO = InputUtil.authenticate(req);

            User user = userService.persist(userDTO).orElseThrow(()->new ApplicationException(ErrorMessage.INTERNAL_ERROR));
            Session userSession = sessionService.startSession(user).orElseThrow(()->new ApplicationException(ErrorMessage.INTERNAL_ERROR));

            Cookie cookie = CookieUtil.createUUIDCookie(userSession);
            resp.addCookie(cookie);
            resp.sendRedirect(req.getContextPath()+"/main");
        }catch (ApplicationException e){
            ExceptionHandler.handle(resp,e);
        }
    }
}
