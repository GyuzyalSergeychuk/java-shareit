package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    UserDto create(User user) throws ValidationException, ConflictException;

    UserDto update(Long id, User user) throws ValidationException, ConflictException;

    List<UserDto> getFindAllUsers();

    UserDto getUserDtoById(Long id);

    User getUserById(Long id);

    boolean deleteUser(Long id);
}
