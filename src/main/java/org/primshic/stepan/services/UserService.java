package org.primshic.stepan.services;

import org.mindrot.jbcrypt.BCrypt;
import org.primshic.stepan.dao.UserRepository;
import org.primshic.stepan.model.User;

import java.util.Optional;

public class UserService {
    private final UserRepository userRepository = new UserRepository();
    public Optional<User> persist(String login, String password) {
        String hashpw = BCrypt.hashpw(password,BCrypt.gensalt(6));
        User user = new User();
        user.setLogin(login);
        user.setPassword(hashpw);
        return userRepository.persist(user);
    }

    public Optional<User> get(String login, String password) {
        return userRepository.get(login,password);
    }
}
