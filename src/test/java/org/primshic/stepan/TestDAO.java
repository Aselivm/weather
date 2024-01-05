package org.primshic.stepan;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.primshic.stepan.dto.account.UserDTO;
import org.primshic.stepan.model.TestUser;
import org.primshic.stepan.services.LocationServiceTest;
import org.primshic.stepan.services.SessionServiceTest;
import org.primshic.stepan.services.UserServiceTest;

class TestDAO {
    private UserServiceTest userServiceTest;
    private SessionServiceTest sessionServiceTest;
    private LocationServiceTest locationServiceTest;

    @BeforeEach
    void init(){
        userServiceTest = new UserServiceTest();
        sessionServiceTest = new SessionServiceTest();
        locationServiceTest = new LocationServiceTest();
    }

    @Test
    void addTestUser(){
        TestUser testUser = new TestUser().builder().login("Stepa").password("parol").build();
        UserDTO userDTO = new UserDTO(testUser.getLogin(),testUser.getPassword());
        TestUser userFromPersist = userServiceTest.persist(userDTO).orElseThrow();
        TestUser userFromGet = userServiceTest.get(userDTO).orElseThrow();
        Assertions.assertEquals(userFromGet,userFromPersist);
    }
}
