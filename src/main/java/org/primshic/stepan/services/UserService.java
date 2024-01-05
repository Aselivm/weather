package org.primshic.stepan.services;

import org.mindrot.jbcrypt.BCrypt;
import org.primshic.stepan.dao.UserRepository;
import org.primshic.stepan.dto.account.UserDTO;
import org.primshic.stepan.model.User;

import java.util.Optional;

public class UserService {
    private final UserRepository userRepository = new UserRepository();
    public Optional<User> persist(UserDTO userDTO) {
        User userEntity = toEntity(userDTO);
        return userRepository.persist(userEntity);
    }

    public Optional<User> get(UserDTO userDTO) {
        User userEntity = toEntity(userDTO);
        return userRepository.get(userEntity.getLogin(),userEntity.getPassword());
    }

    private String hashPassword(String password){
       return BCrypt.hashpw(password,BCrypt.gensalt(6));
    }

    private User toEntity(UserDTO userDTO){
        String hashpw = hashPassword(userDTO.getPassword());
        User user = new User();
        user.setLogin(userDTO.getLogin());
        user.setPassword(hashpw);
        return user;
    }
}
