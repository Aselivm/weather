package org.primshic.stepan.test_cases;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.primshic.stepan.web.auth.user.User;
import org.primshic.stepan.web.auth.user.UserDTO;
import org.primshic.stepan.web.auth.user.UserService;
import org.primshic.stepan.repo_mock.TestHibernateUtil;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {
    private static UserService userServiceTest;

    private static User testUser;

    private static User userFromPersist;

    @BeforeAll
    static void init(){
        SessionFactory sessionFactory = TestHibernateUtil.getSessionFactory();
        userServiceTest = new UserService(sessionFactory);
        testUser = new User().builder().login("StepaXX").password("parol").build();
    }

    @Test
    @Order(1)
    @DisplayName("Test User Persistence")
    void addTestUser(){
        Assertions.assertNull(userFromPersist);

        User testUser = new User().builder().login("StepaXX").password("parol").build();
        UserDTO userDTO = new UserDTO(testUser.getLogin(),testUser.getPassword());

        userFromPersist = userServiceTest.persist(userDTO).orElseThrow();
        Assertions.assertNotNull(userFromPersist);
    }


    @Test
    @Order(2)
    @DisplayName("Check User Persistence Return")
    void checkPersistReturn() {
        UserDTO userDTO = new UserDTO(testUser.getLogin(), testUser.getPassword());

        User userFromGet = userServiceTest.get(userDTO).orElseThrow();
        System.out.println("User from Persist: " + userFromPersist);
        System.out.println("User from Get: " + userFromGet);
        Assertions.assertEquals(userFromGet.getLogin(), userFromPersist.getLogin());

    }


    @Test
    @Order(3)
    @DisplayName("Check Unique Login")
    void checkUniqueLogin(){
        UserDTO userDTO2 = new UserDTO(testUser.getLogin(),testUser.getPassword());

        Assertions.assertThrows(RuntimeException.class, () -> userServiceTest.persist(userDTO2));
    }

}
