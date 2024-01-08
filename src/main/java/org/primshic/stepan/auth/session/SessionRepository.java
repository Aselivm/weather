package org.primshic.stepan.auth.session;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.primshic.stepan.auth.user.User;

import java.util.Optional;

@Slf4j
public class SessionRepository{

    private final SessionFactory sessionFactory;

    public SessionRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Optional<Session> getByUserId(int id, org.hibernate.Session session) {
        return Optional.ofNullable(session.createQuery("FROM Session WHERE user.id = :userId", Session.class)
                .setParameter("userId", id)
                .uniqueResult());
    }

    public Optional<Session> getById(String uuid) {
        try (org.hibernate.Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Session.class, uuid));
        } catch (Exception e) {
            log.error("Error while getting session by ID", e);
            throw e;
        }
    }

    public Optional<Session> getById(String uuid, org.hibernate.Session session) {
        return Optional.ofNullable(session.get(Session.class, uuid));
    }

    public void deleteExpiredSessions() {
        try (org.hibernate.Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createQuery("delete from TestSession where expiresAt<CURRENT_TIMESTAMP ").executeUpdate();
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            log.error("Error while deleting expired sessions", e);
            throw e;
        }
    }

    public Optional<Session> startSession(Session sessionEntity) {
        Optional<Session> fromDB;
        User user = sessionEntity.getUser();
        try (org.hibernate.Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            removeExistingSession(user, session);
            session.save(sessionEntity);

            fromDB = getByUserId(user.getId(), session);

            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("Error while starting session", e);
            throw e;
        }

        return fromDB;
    }

    private void removeExistingSession(User user, org.hibernate.Session session) {
        Optional<Session> existingSession = getByUserId(user.getId(), session);
        existingSession.ifPresent(session::delete);
    }

    public void delete(Session sessionEntity) {
        try (org.hibernate.Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(sessionEntity);
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("Error while deleting session", e);
            throw e;
        }
    }
}
