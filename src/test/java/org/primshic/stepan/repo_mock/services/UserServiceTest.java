package org.primshic.stepan.repo_mock.services;

import org.mindrot.jbcrypt.BCrypt;
import org.primshic.stepan.repo_mock.dao.UserRepositoryTest;
import org.primshic.stepan.dto.account.UserDTO;
import org.primshic.stepan.repo_mock.model.TestUser;

import java.util.Optional;

public class UserServiceTest {
    private final UserRepositoryTest userRepository = new UserRepositoryTest();

    public Optional<TestUser> persist(UserDTO userDTO) {
        TestUser userEntity = toEntity(userDTO);
        return userRepository.persist(userEntity);
    }

    public Optional<TestUser> get(UserDTO userDTO) {
        TestUser userEntity = toEntity(userDTO);
        TestUser testUser = userRepository.get(userEntity.getLogin()).orElseThrow();//todo add exception
        if(!BCrypt.checkpw(userDTO.getPassword(), testUser.getPassword())){
            throw new RuntimeException("Wrong password"); //todo wrong password exception
        }
        return Optional.of(testUser);
    }

    private TestUser toEntity(UserDTO userDTO){
        String hashpw = hashPassword(userDTO.getPassword());
        TestUser user = new TestUser();
        user.setLogin(userDTO.getLogin());
        user.setPassword(hashpw);
        return user;
    }

    private String hashPassword(String password){
        return BCrypt.hashpw(password,BCrypt.gensalt(10));
    }
}
