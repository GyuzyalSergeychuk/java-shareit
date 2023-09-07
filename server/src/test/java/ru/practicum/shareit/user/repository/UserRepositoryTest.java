package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    @Test
    void findByEmailNotSelfTest() {
        var id = 3L;
        var user = new User();
        user.setName("user");
        user.setEmail("user@user.com");
        var user1 = new User();
        user1.setName("user1");
        user1.setEmail("user@user.com1");
        var user2 = new User();
        user2.setName("user2");
        user2.setEmail("user@user.com2");
        entityManager.persist(user);
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();

        var actualResponse = this.userRepository.findByEmailNotSelf(user.getEmail(), id);

        assertEquals(user, actualResponse);
    }
}