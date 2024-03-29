package org.primshic.stepan.web.auth.user;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.primshic.stepan.exception.ApplicationException;
import org.primshic.stepan.exception.ErrorMessage;

import javax.persistence.PersistenceException;
import java.util.Optional;

@Slf4j
public class UserRepository{

    private final SessionFactory sessionFactory;

    public UserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Optional<User> persist(User user) {
        try (org.hibernate.Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }catch (PersistenceException ex) {
            log.warn("Error persisting user: {}", ex.getMessage());
            throw new ApplicationException(ErrorMessage.LOGIN_ALREADY_EXISTS);
        } catch (Exception e) {
            log.error("Error while persisting user", e);
            throw new ApplicationException(ErrorMessage.DATABASE_ERROR);
        }
        return Optional.of(user);
    }

    public Optional<User> get(String login) {
        try (org.hibernate.Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.createQuery
                            ("FROM User WHERE login = :login", User.class)
                    .setParameter("login", login)
                    .uniqueResult());
        } catch (Exception e) {
            log.error("Error while getting user by login", e);
            throw new ApplicationException(ErrorMessage.DATABASE_ERROR);
        }
    }
}
