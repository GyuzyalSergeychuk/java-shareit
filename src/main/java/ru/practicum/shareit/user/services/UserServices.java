package ru.practicum.shareit.user.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServices {

    private final UserStorage userStorage;

    public UserDto create(User user) throws ValidationException, ConflictException {

        return userStorage.create(user);
    }

    public UserDto update(Long id, User user) throws ValidationException, ConflictException {
        return userStorage.update(id, user);
    }

    public List<UserDto> getFindAllUsers() {
        return userStorage.getFindAllUsers();
    }

    public UserDto getUserId(Long id) {
        return userStorage.getUserId(id);
    }

    public boolean deleteUser(Long id) {
        return userStorage.deleteUser(id);
    }
}
