package ru.practicum.shareit.pagination;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.data.DataFactory.getUser;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class PaginationTest {

    @InjectMocks
    private Pagination<User> pagination;

    @Test
    void makePaginationTest() throws ValidationException {
        var from = 0;
        var size = 20;

        var user = getUser(1L, "user", "user@user.com");
        var user1 = getUser(2L, "user2", "user@user.com2");
        var user2 = getUser(3L, "user3", "user@user.com3");

        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user1);
        users.add(user2);

        var actualResponse = pagination.makePagination(from, size, users);

        assertEquals(users, actualResponse);
        assertEquals(users.size(), actualResponse.size());
        assertEquals(users.get(1).getName(), actualResponse.get(1).getName());
    }

    @Test
    void makePaginationMinusTest() {
        var from = -1;
        var size = 0;
        var user = getUser(1L, "user", "user@user.com");
        List<User> users = List.of(user);

        assertThrows(ValidationException.class,
                () -> pagination.makePagination(from, size, users),
                "Индекс первого элемента и размер листа не может быть меньше нуля");
    }
}