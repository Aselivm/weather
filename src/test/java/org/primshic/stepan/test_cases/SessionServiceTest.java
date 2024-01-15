package org.primshic.stepan.test_cases;

import org.junit.jupiter.api.*;
import org.primshic.stepan.web.auth.session.Session;
import org.primshic.stepan.web.auth.session.SessionService;
import org.primshic.stepan.web.auth.user.User;
import org.primshic.stepan.web.auth.user.UserDTO;
import org.primshic.stepan.web.auth.user.UserService;
import org.primshic.stepan.repo_mock.TestHibernateUtil;

import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SessionServiceTest {

    private static UserService userService;

    private static SessionService sessionService;

    private static User userFromPersist;

    private static Optional<Session> testSession;

    @BeforeAll
    static void setUp() {
        sessionService = new SessionService(TestHibernateUtil.getSessionFactory());
        userService = new UserService(TestHibernateUtil.getSessionFactory());
    }

    @Test
    @Order(1)
    @DisplayName("Test User Persistence")
    void addTestUser(){
        Assertions.assertNull(userFromPersist);

        User testUser = new User().builder().login("StepaXX").password("parol").build();
        UserDTO userDTO = new UserDTO(testUser.getLogin(),testUser.getPassword());

        userFromPersist = userService.persist(userDTO).orElseThrow();
        Assertions.assertNotNull(userFromPersist);
    }


    @Test
    @Order(2)
    @DisplayName("Start Session - Success")
    void startSession_Success() {
        testSession = sessionService.startSession(userFromPersist);
        Assertions.assertTrue(testSession.isPresent());
    }

    @Test
    @Order(3)
    @DisplayName("Get Session By ID - Success")
    void getById_Success() {

        Optional<Session> result = sessionService.getById(testSession.orElseThrow().getId());
        Assertions.assertEquals(
                result.orElseThrow().getId(),
                testSession.get().getId()
        );
    }

    @Test
    @Order(4)
    @DisplayName("Get Session By ID - Not Found")
    void getById_NotFound() {
        String sessionId = "nonExistentSessionId";
        Optional<Session> result = sessionService.getById(sessionId);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @Order(5)
    @DisplayName("Delete Session")
    void deleteSession() {
        sessionService.delete(testSession.orElseThrow());
        testSession = sessionService.getById(testSession.orElseThrow().getId());
        Assertions.assertFalse(testSession.isPresent());
    }
}