package org.primshic.stepan.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.primshic.stepan.model.Session;
import org.primshic.stepan.services.SessionService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionUtil {
    @Deprecated
    public static Optional<Session> getSessionByReq(HttpServletRequest req) {
        Session userSession = (Session) req.getAttribute("userSession");
        if (userSession == null) {
            log.warn("User session not found in the request.");
        }

        return Optional.ofNullable(userSession);
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

    public static void deleteSessionIfPresent(String sessionId, SessionService sessionService) {
        if (validSessionId(sessionId)) {
            sessionService.getById(sessionId).ifPresent(session -> {
                sessionService.delete(session);
                log.info("Deleted session with ID: {}", sessionId);
            });
        } else {
            log.warn("Invalid session ID: {}", sessionId);
        }
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
