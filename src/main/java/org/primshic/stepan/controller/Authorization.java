package org.primshic.stepan.controller;

import lombok.extern.slf4j.Slf4j;
import org.primshic.stepan.dto.user.UserDTO;
import org.primshic.stepan.exception.ApplicationException;
import org.primshic.stepan.exception.ErrorMessage;
import org.primshic.stepan.model.Session;
import org.primshic.stepan.model.User;
import org.primshic.stepan.services.SessionService;
import org.primshic.stepan.services.UserService;
import org.primshic.stepan.util.InputUtil;
import org.primshic.stepan.util.SessionUtil;
import org.primshic.stepan.util.ThymeleafUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@WebServlet(urlPatterns = "/auth")
@Slf4j
public class Authorization extends HttpServlet {
    private UserService userService;

    private SessionService sessionService;

    @Override
    public void init() throws ServletException {
        userService = (UserService) getServletConfig().getServletContext().getAttribute("userService");
        sessionService = (SessionService) getServletConfig().getServletContext().getAttribute("sessionService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Optional<Session> optionalUserSession = SessionUtil.getSessionFromCookies(req,sessionService);
        try {
            ThymeleafUtil.templateEngineProcessWithVariables("authorization", req, resp, new HashMap<>(){{
                put("userSession", optionalUserSession);
            }});
        } catch (ApplicationException e) {
            log.error("Error processing GET request in Authorization: {}", e.getMessage(), e);
            ThymeleafUtil.templateEngineProcessWithVariables("authorization", req, resp, new HashMap<>(){{
                put("userSession", optionalUserSession);
                put("error", e.getError());
            }});
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Optional<Session> optionalUserSession = SessionUtil.getSessionFromCookies(req,sessionService);
        try {
            UserDTO userDTO = InputUtil.authenticate(req);

            User user = userService.get(userDTO).orElseThrow(() -> new ApplicationException(ErrorMessage.INTERNAL_ERROR));
            Session userSession = sessionService.startSession(user).orElseThrow(() -> new ApplicationException(ErrorMessage.INTERNAL_ERROR));

            String uuid = userSession.getId();
            Cookie cookie = new Cookie("uuid", uuid);

            resp.addCookie(cookie);
            resp.sendRedirect(req.getContextPath() + "/main");
        } catch (ApplicationException e) {
            log.error("Error processing POST request in Authorization: {}", e.getMessage(), e);
            ThymeleafUtil.templateEngineProcessWithVariables("authorization", req, resp, new HashMap<>(){{
                put("userSession", optionalUserSession);
                put("error", e.getError());
            }});
        }
    }
}
