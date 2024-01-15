package org.primshic.stepan.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.primshic.stepan.web.auth.session.Session;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WebContextUtil {
    public static WebContext createContext(HttpServletRequest req, HttpServletResponse resp,
                                           ServletContext servletContext) {
        WebContext context = new WebContext(req, resp, servletContext);
        Optional<Session> optionalUserSession = SessionUtil.getSessionByReq(req);
        context.setVariable("userSession", optionalUserSession);
        return context;
    }
}
