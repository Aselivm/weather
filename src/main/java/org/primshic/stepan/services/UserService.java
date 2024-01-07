package org.primshic.stepan.services;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.mindrot.jbcrypt.BCrypt;
import org.primshic.stepan.dao.UserRepository;
import org.primshic.stepan.dto.user.UserDTO;
import org.primshic.stepan.exception.ApplicationException;
import org.primshic.stepan.exception.ErrorMessage;
import org.primshic.stepan.model.User;

import javax.persistence.PersistenceException;
import java.util.Optional;

@Slf4j
public class UserService {

    private final UserRepository userRepository;
    public UserService(SessionFactory sessionFactory) {
        this.userRepository = new UserRepository(sessionFactory);
    }

    public Optional<User> persist(UserDTO userDTO) {
        Optional<User> user;
        try {
            User userEntity = toEntity(userDTO);
            user = userRepository.persist(userEntity);
        } catch (PersistenceException ex) {
            log.warn("Error persisting user: {}", ex.getMessage());
            throw new ApplicationException(ErrorMessage.LOGIN_ALREADY_EXISTS);
        }
        return user;
    }

    public Optional<User> get(UserDTO userDTO) {
        User userEntity = toEntity(userDTO);
        User user = userRepository.get(userEntity.getLogin())
                    .orElseThrow(()->new ApplicationException(ErrorMessage.LOGIN_NOT_EXIST));

        if (!BCrypt.checkpw(userDTO.getPassword(), user.getPassword())) {
            log.warn("Wrong password for user: {}", userDTO.getLogin());
            throw new ApplicationException(ErrorMessage.WRONG_PASSWORD);
        }
        return Optional.of(user);
    }

    private User toEntity(UserDTO userDTO) {
        String hashpw = hashPassword(userDTO.getPassword());
        User user = new User();
        user.setLogin(userDTO.getLogin());
        user.setPassword(hashpw);
        return user;
    }

    private String hashPassword(String password) {
        final int ROUNDS = 6;
        return BCrypt.hashpw(password, BCrypt.gensalt(ROUNDS));
    }
}
