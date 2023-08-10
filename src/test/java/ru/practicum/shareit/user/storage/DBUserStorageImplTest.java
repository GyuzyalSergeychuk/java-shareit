package ru.practicum.shareit.user.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static ru.practicum.shareit.data.DataFactory.getUser;
import static ru.practicum.shareit.data.DataFactory.getUserDto;

@ExtendWith(MockitoExtension.class)
class DBUserStorageImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private DBUserStorageImpl dbUserStorage;

    @Test
    void createTest() throws ValidationException, ConflictException {
        var  user = getUser(1L, "user", "user@user.com");
        var userDto = getUserDto(1L, "user", "user@user.com");

        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userMapper.toUserDto(user)).thenReturn(userDto);

        var actualResponse = dbUserStorage.create(user);

        assertEquals(userDto , actualResponse);
    }


    @Test
    void updateTest() {
    }

    @Test
    void getFindAllUsersTest() {
    }

    @Test
    void getUserDtoByIdTest() {
    }

    @Test
    void getUserByIdTest() {
    }

    @Test
    void deleteUserTest() {
    }
}