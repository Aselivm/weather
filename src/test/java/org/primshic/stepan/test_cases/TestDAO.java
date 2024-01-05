package org.primshic.stepan.test_cases;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.primshic.stepan.dto.account.UserDTO;
import org.primshic.stepan.repo_mock.model.TestSession;
import org.primshic.stepan.repo_mock.model.TestUser;
import org.primshic.stepan.repo_mock.services.LocationServiceTest;
import org.primshic.stepan.repo_mock.services.SessionServiceTest;
import org.primshic.stepan.repo_mock.services.UserServiceTest;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

class TestDAO {
    private static Logger log = Logger.getLogger(TestDAO.class.getName());
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
        TestUser testUser = new TestUser().builder().login("StepaXX").password("parol").build();
        UserDTO userDTO = new UserDTO(testUser.getLogin(),testUser.getPassword());
        TestUser userFromPersist = userServiceTest.persist(userDTO).orElseThrow();
        TestUser userFromGet = userServiceTest.get(userDTO).orElseThrow();
        Assertions.assertEquals(userFromGet,userFromPersist);
    }


    @Test
    void checkUniqueLogin(){
        TestUser testUser = new TestUser().builder().login("StepaNN").password("parol").build();
        UserDTO userDTO = new UserDTO(testUser.getLogin(),testUser.getPassword());

        userServiceTest.persist(userDTO).orElseThrow();

        TestUser testUser2 = new TestUser().builder().login("StepaNN").password("parol2").build();
        UserDTO userDTO2 = new UserDTO(testUser2.getLogin(),testUser2.getPassword());

        Assertions.assertThrows(RuntimeException.class, () -> userServiceTest.persist(userDTO2));
    }


    @Test
    void testSessionExpire(){
        TestUser testUser = new TestUser().builder().login("Stepa").password("parol").build();
        UserDTO userDTO = new UserDTO(testUser.getLogin(),testUser.getPassword());

        TestUser userFromPersist = userServiceTest.persist(userDTO).orElseThrow();
        TestSession testSession = sessionServiceTest.startSession(userFromPersist).orElseThrow();
        CountDownLatch latch = new CountDownLatch(1);

        new Thread(() -> {
            try {
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        }).start();

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assertions.assertFalse(sessionServiceTest.getById(testSession.getId()).isPresent());
    }
}
