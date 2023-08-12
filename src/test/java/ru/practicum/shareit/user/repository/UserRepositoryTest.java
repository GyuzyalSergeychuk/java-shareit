package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.data.DataFactory.getUser;

@RunWith(SpringRunner.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    UserRepositoryTest() {
    }

    @Test
    void findByEmailNotSelf() {
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

    @Test
    void findById() {
        var id = 1L;
        var user = new User();
        user.setName("user");
        user.setEmail("user@user.com");
        var user1 = Optional.of(getUser(1L, "user", "user@user.com"));
        entityManager.persist(user);
        entityManager.flush();

        var actualResponse = userRepository.findById(id);

        assertEquals(user1, actualResponse);
    }
}