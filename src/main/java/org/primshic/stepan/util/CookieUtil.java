package org.primshic.stepan.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.primshic.stepan.model.Session;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieUtil {

    public static String getSessionIdByCookie(Cookie[] cookies) {
        if (cookies == null || cookies.length == 0) {
            log.info("No cookies found.");
            return null;
        }

        String sessionId = Arrays.stream(cookies)
                .filter(cookie -> Objects.equals(cookie.getName(), "uuid"))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        if (sessionId != null) {
            log.info("Session ID found in cookie: {}", sessionId);
        } else {
            log.warn("No session ID found in cookies.");
        }

        return sessionId;
    }

    public static void createSessionCookie(HttpServletResponse resp, String sessionId) {
        Cookie cookie = new Cookie("uuid", sessionId);
        cookie.setMaxAge(-1); // Кука будет удалена при закрытии браузера
        resp.addCookie(cookie);
    }

}