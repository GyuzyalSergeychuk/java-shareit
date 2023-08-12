package ru.practicum.shareit.user.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.services.UserServices;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.data.DataFactory.getUser;
import static ru.practicum.shareit.data.DataFactory.getUserDto;

@ExtendWith(MockitoExtension.class)
class UserServicesTest {

    @Mock
    private UserStorage userStorage;

    @InjectMocks
    private UserServices userServices;

    @Test
    void create() throws ValidationException, ConflictException {
        var user = getUser(1L, "user", "user@user.com");
        var userDto = getUserDto(1L, "user", "user@user.com");

        when(userStorage.create(user)).thenReturn(userDto);

        var actualResponse = userServices.create(user);

        assertEquals(userDto, actualResponse);
    }

    @Test
    void update() throws ValidationException, ConflictException {
        var id = 1L;
        var user = getUser(1L, "user", "user@user.com");
        var userDto = getUserDto(1L, "user", "user@user.com");

        when(userStorage.update(id, user)).thenReturn(userDto);

        var actualResponse = userServices.update(id, user);

        assertEquals(userDto, actualResponse);
    }

    @Test
    void getFindAllUsers() {
    }

    @Test
    void getUserId() {
    }

    @Test
    void deleteUser() {
    }
}
