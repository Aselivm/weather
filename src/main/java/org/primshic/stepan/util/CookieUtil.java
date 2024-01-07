package org.primshic.stepan.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.primshic.stepan.model.Session;

import javax.servlet.http.Cookie;
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

    public static Cookie createUUIDCookie(Session session) {
        Cookie uuidCookie = new Cookie("uuid", session.getId());
        log.info("Created UUID cookie for session ID: {}", session.getId());
        return uuidCookie;
    }
}