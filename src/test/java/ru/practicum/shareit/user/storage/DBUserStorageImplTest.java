package ru.practicum.shareit.user.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
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
        var user = getUser(1L, "user", "user@user.com");
        var userDto = getUserDto(1L, "user", "user@user.com");

        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        var actualResponse = dbUserStorage.create(user);

        assertEquals(userDto , actualResponse);
    }

    @Test
    void updateSuccessTest() throws ValidationException, ConflictException {
        var id = 1L;
        var user = getUser(1L, "user", "user@user.com");
        var userDto = getUserDto(1L, "user", "user@user.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.findByEmailNotSelf(user.getEmail(), user.getId())).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        var actualResponse = dbUserStorage.update(id, user);

        assertEquals(userDto , actualResponse);
    }

    @Test
    void updateId0Test() {
        var id = 0L;
        var user = getUser(1L, "user", "user@user.com");

        assertThrows(ObjectNotFoundException.class,
                () ->  dbUserStorage.update(id, user),
                "Пользователь не найден");
    }

    @Test
    void updateEmailAndUserIdTest(){
        var id = 1L;
        var user = getUser(1L, "user", "user@user.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.findByEmailNotSelf(user.getEmail(), user.getId())).thenReturn(user);

        assertThrows(ConflictException.class,
                () ->  dbUserStorage.update(id, user));
    }

    @Test
    void updateNameNullTest() throws ValidationException, ConflictException {
        var id = 1L;
        var user = getUser(1L, null, "user@user.com");
        var userBase = getUser(1L, "user1", "user@user.com");
        var userDto = getUserDto(1L, "user1", "user@user.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(userBase));
        when(userRepository.findByEmailNotSelf(user.getEmail(), user.getId())).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        var actualResponse = dbUserStorage.update(id, user);

        assertEquals(userDto, actualResponse);
    }

    @Test
    void updateEmailNullTest() throws ValidationException, ConflictException {
        var id = 1L;
        var user = getUser(1L, "user1", null);
        var userBase = getUser(1L, "user1", "user@user.com");
        var userDto = getUserDto(1L, "user1", "user@user.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(userBase));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        var actualResponse = dbUserStorage.update(id, user);

        assertEquals(userDto, actualResponse);
    }


    @Test
    void getFindAllUsersTest() {
        var user = getUser(1L, "user1", "user@user.com");
        var userDto = getUserDto(1L, "user1", "user@user.com");
        List<User> userList = List.of(user);
        List<UserDto> userDtoList = List.of(userDto);

        when(userRepository.findAll()).thenReturn(userList);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        var actualResponse = dbUserStorage.getFindAllUsers();

        assertEquals(userDtoList, actualResponse);
    }

    @Test
    void getUserDtoByIdTest() {
        var id = 1L;
        var user = getUser(1L, "user", "user@user.com");
        var userDto = getUserDto(1L, "user", "user@user.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        var actualResponse = dbUserStorage.getUserDtoById(id);

        assertEquals(userDto, actualResponse);
    }

    @Test
    void getUserDtoById0Test() {
        var id = 0L;
        var user = getUser(1L, "user", "user@user.com");

        assertThrows(ObjectNotFoundException.class,
                () ->  dbUserStorage.getUserDtoById(id),
                "Пользователь не найден");
    }

    @Test
    void getUserByIdTest() {
        var id = 1L;
        var user = getUser(1L, "user", "user@user.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        var actualResponse = dbUserStorage.getUserById(id);

        assertEquals(user, actualResponse);
    }

    @Test
    void getUserById0Test() {
        var id = 0L;
        var user = getUser(1L, "user", "user@user.com");

        assertThrows(ObjectNotFoundException.class,
                () ->  dbUserStorage.getUserById(id),
                "Пользователь не найден");
    }

    @Test
    void deleteUserTest() {
        var id = 1L;
        var user = getUser(1L, "user", "user@user.com");
        var expectedResponse = true;

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        var actualResponse = dbUserStorage.deleteUser(id);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void deleteUserId0Test() {
        var id = 0L;
        var user = getUser(1L, "user", "user@user.com");

        assertThrows(ObjectNotFoundException.class,
                () ->  dbUserStorage.deleteUser(id),
                "Пользователь не найден");
    }

}