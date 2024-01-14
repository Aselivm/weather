package org.primshic.stepan.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.primshic.stepan.auth.session.Session;
import org.primshic.stepan.auth.session.SessionService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionUtil {

    public static Optional<Session> getSessionByReq(HttpServletRequest req) {
        Optional<Session> userSession = (Optional<Session>) req.getSession().getAttribute("userSession");
        if (userSession.isEmpty()) {
            log.warn("User session not found in the request.");
        }

        return userSession;
    }
    public static Optional<Session> getCurrentSession(String sessionId,SessionService sessionService) {
        Optional<Session> userSession = Optional.empty();
        if (validSessionId(sessionId)) {
            log.info("Session ID is valid: {}", sessionId);
            userSession = sessionService.getById(sessionId);
            log.info("Retrieved session by ID: {}", sessionId);
        } else {
            log.warn("Invalid session ID: {}", sessionId);
        }
        return userSession;
    }

    public static void deleteSessionIfPresent(Optional<Session> userSession, SessionService sessionService) {
        userSession.ifPresent(session -> {
            sessionService.delete(session);
            log.info("Session deleted: {}", session.getId());
        });
    }

    public static Optional<Session> getSessionFromCookies(HttpServletRequest req,SessionService sessionService) {
        String sessionId = CookieUtil.getSessionIdByCookie(req.getCookies());
        return getCurrentSession(sessionId,sessionService);
    }

    public static boolean validSessionId(String sessionId) {
        boolean isValid = sessionId != null && !sessionId.isEmpty();
        if (!isValid) {
            log.warn("Invalid session ID: {}", sessionId);
        }
        return isValid;
    }
}
