package org.primshic.stepan.services;

import org.primshic.stepan.model.User;

public class UserService {
    public User persist(String username, String login, String password) {
        //todo BCrypt хеширование пароля
        return null;
    }

    public User get(String login, String password) {
        return null;
    }
}
