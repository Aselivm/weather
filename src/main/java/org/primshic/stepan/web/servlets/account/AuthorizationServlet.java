package org.primshic.stepan.web.servlets.account;

import lombok.extern.slf4j.Slf4j;
import org.primshic.stepan.util.WebContextUtil;
import org.primshic.stepan.web.auth.session.Session;
import org.primshic.stepan.web.auth.user.User;
import org.primshic.stepan.web.auth.user.UserDTO;
import org.primshic.stepan.web.auth.user.UserService;
import org.primshic.stepan.exception.ApplicationException;
import org.primshic.stepan.exception.ErrorMessage;
import org.primshic.stepan.web.auth.session.SessionService;
import org.primshic.stepan.util.CookieUtil;
import org.primshic.stepan.util.InputUtil;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/auth")
@Slf4j
public class AuthorizationServlet extends HttpServlet {
    private UserService userService;

    private  TemplateEngine templateEngine;

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
        WebContext context = WebContextUtil.createContext(req, resp, getServletContext());
        templateEngine.process("authorization", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebContext context = WebContextUtil.createContext(req, resp, getServletContext());
        try {
            UserDTO userDTO = InputUtil.authenticate(req);

            User user = userService.get(userDTO)
                    .orElseThrow(() -> new ApplicationException(ErrorMessage.INTERNAL_ERROR));
            Session userSession = sessionService.startSession(user)
                    .orElseThrow(() -> new ApplicationException(ErrorMessage.INTERNAL_ERROR));

            CookieUtil.createSessionCookie(resp, userSession.getId());

            resp.sendRedirect(req.getContextPath() + "/main");
        } catch (ApplicationException e) {
            context.setVariable("error",e.getError());
            log.error("Error processing POST request in Registration: {}", e.getMessage(), e);
            templateEngine.process("authorization", context, resp.getWriter());
        }
    }
}
