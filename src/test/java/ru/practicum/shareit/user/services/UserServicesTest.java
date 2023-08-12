package ru.practicum.shareit.user.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.ValidationException;

import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

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
        var userDto = getUserDto(1L, "user", "user@user.com");
        var users = List.of(userDto);
        var userTest = List.of(userDto);

        when(userStorage.getFindAllUsers()).thenReturn(users);

        var actualResponse = userServices.getFindAllUsers();

        assertEquals(userTest, actualResponse);
    }

    @Test
    void getUserId() {
        var id = 1L;
        var userDto = getUserDto(1L, "user", "user@user.com");

        when(userStorage.getUserDtoById(id)).thenReturn(userDto);

        var actualResponse = userServices.getUserId(id);

        assertEquals(userDto, actualResponse);
    }

    @Test
    void deleteUser() {
        var id = 1L;
        var isResult = true;

        when(userStorage.deleteUser(id)).thenReturn(true);

        var actualResponse = userServices.deleteUser(id);

        assertEquals(isResult, actualResponse);
    }
}