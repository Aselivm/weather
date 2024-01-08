package org.primshic.stepan.test_cases;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.primshic.stepan.auth.session.SessionRepository;
import org.primshic.stepan.auth.user.UserDTO;
import org.primshic.stepan.repo_mock.model.TestSession;
import org.primshic.stepan.repo_mock.model.TestUser;
import org.primshic.stepan.repo_mock.services.SessionServiceTest;
import org.primshic.stepan.repo_mock.services.UserServiceTest;
import org.primshic.stepan.repo_mock.util.TestHibernateUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SessionLifeCycleTest {
    private static UserServiceTest userServiceTest;
    private static SessionServiceTest sessionServiceTest;
    private static ScheduledExecutorService executorService;

    private static TestUser userFromPersist;

    @BeforeAll
    static void init(){
        SessionFactory sessionFactory = TestHibernateUtil.getSessionFactory();
        userServiceTest = new UserServiceTest(sessionFactory);
        sessionServiceTest = new SessionServiceTest(sessionFactory);
    }

    @Test
    @Order(1)
    @DisplayName("Add Test User")
    void addTestUser(){
        TestUser testUser = new TestUser().builder().login("StepaXX").password("parol").build();
        UserDTO userDTO = new UserDTO(testUser.getLogin(),testUser.getPassword());
        userFromPersist = userServiceTest.persist(userDTO).orElseThrow();
        TestUser userFromGet = userServiceTest.get(userDTO).orElseThrow();
        Assertions.assertEquals(userFromGet,userFromPersist);
    }


    @Test
    @Order(2)
    @DisplayName("Check Unique Login")
    void checkUniqueLogin(){

        TestUser testUser2 = new TestUser().builder().login("StepaXX").password("parol2").build();
        UserDTO userDTO2 = new UserDTO(testUser2.getLogin(),testUser2.getPassword());

        Assertions.assertThrows(RuntimeException.class, () -> userServiceTest.persist(userDTO2));
    }


    @Test
    @Order(3)
    @DisplayName("Test Session Expire")
    void testSessionExpire(){
        startSchedule();
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

    private void startSchedule() {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate
                (new SessionRepository
                        (TestHibernateUtil.getSessionFactory())
                        ::deleteExpiredSessions, 6, 5, TimeUnit.SECONDS);
    }
}
