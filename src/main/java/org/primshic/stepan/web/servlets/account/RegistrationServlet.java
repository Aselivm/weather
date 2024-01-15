package org.primshic.stepan.web.servlets.account;

import lombok.extern.slf4j.Slf4j;
import org.primshic.stepan.web.auth.session.Session;
import org.primshic.stepan.web.auth.user.User;
import org.primshic.stepan.web.auth.user.UserDTO;
import org.primshic.stepan.web.auth.user.UserService;
import org.primshic.stepan.exception.ApplicationException;
import org.primshic.stepan.exception.ErrorMessage;
import org.primshic.stepan.web.auth.session.SessionService;
import org.primshic.stepan.util.CookieUtil;
import org.primshic.stepan.util.InputUtil;
import org.primshic.stepan.web.servlets.WeatherTrackerBaseServlet;
import org.thymeleaf.TemplateEngine;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/reg")
@Slf4j
public class RegistrationServlet extends WeatherTrackerBaseServlet {
    private UserService userService;

    private TemplateEngine templateEngine;

    private SessionService sessionService;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        userService = (UserService) servletContext.getAttribute("userService");
        templateEngine = (TemplateEngine) servletContext.getAttribute("templateEngine");
        sessionService = (SessionService) servletContext.getAttribute("sessionService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        templateEngine.process("registration", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            UserDTO userDTO = InputUtil.authenticate(req);

            User user = userService.persist(userDTO)
                    .orElseThrow(() -> new ApplicationException(ErrorMessage.INTERNAL_ERROR));
            Session userSession = sessionService.startSession(user)
                    .orElseThrow(() -> new ApplicationException(ErrorMessage.INTERNAL_ERROR));

            CookieUtil.createSessionCookie(resp,userSession.getId());
            resp.sendRedirect(req.getContextPath() + "/main");
        } catch (ApplicationException e) {
            context.setVariable("error",e.getError());
            log.error("Error processing POST request in Registration: {}", e.getMessage(), e);
            templateEngine.process("registration", context, resp.getWriter());
        }
    }
}

