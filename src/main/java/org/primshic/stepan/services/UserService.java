package org.primshic.stepan.services;

import org.mindrot.jbcrypt.BCrypt;
import org.primshic.stepan.dao.UserRepository;
import org.primshic.stepan.dto.account.UserDTO;
import org.primshic.stepan.exception.ApplicationException;
import org.primshic.stepan.exception.ErrorMessage;
import org.primshic.stepan.model.User;

import javax.persistence.PersistenceException;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository = new UserRepository();
    public Optional<User> persist(UserDTO userDTO) {
        Optional<User> user;
        try{
            User userEntity = toEntity(userDTO);
            user = userRepository.persist(userEntity);
        }catch (PersistenceException ex){
            throw new ApplicationException(ErrorMessage.LOGIN_ALREADY_EXIST);
        }
        return user;
    }

    public Optional<User> get(UserDTO userDTO) {
        User userEntity = toEntity(userDTO);
        User user = userRepository.get(userEntity.getLogin()).orElseThrow();//todo add exception
        if(!BCrypt.checkpw(userDTO.getPassword(), user.getPassword())){
            throw new ApplicationException(ErrorMessage.WRONG_PASSWORD);
        }
        return Optional.of(user);
    }

    private User toEntity(UserDTO userDTO){
        String hashpw = hashPassword(userDTO.getPassword());
        User user = new User();
        user.setLogin(userDTO.getLogin());
        user.setPassword(hashpw);
        return user;
    }

    private String hashPassword(String password){
        return BCrypt.hashpw(password,BCrypt.gensalt(6));
    }
}
