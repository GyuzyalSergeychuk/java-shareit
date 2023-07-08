package ru.practicum.shareit.user.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.ErrorHandler;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {

    private final UserMapper userMapper;

    private HashMap<Long, UserDto> users = new HashMap<>();

    @Override
    public UserDto create(User user) throws ValidationException, ConflictException {
            User afterCheckUser = standardCheck(user);
            afterCheckUser.assignId();
            UserDto userDto = userMapper.toUserDto(afterCheckUser);
            users.put(userDto.getId(), userDto);
            log.info("Пользователь успешно добавлен {}", user.getId());
            return userDto;
    }

    @Override
    public UserDto update(Long id, User user) throws ObjectNotFoundException, ConflictException {

        for (UserDto value : users.values()) {
            if(value.getEmail().equals(user.getEmail())){
                throw new ConflictException("email уже существует");
            }
        }

        UserDto userDto = userMapper.toUserDto(user);
        userDto.setId(id);

        if (userDto.getId() <= 0) {
            throw new ObjectNotFoundException("Пользователь не найдет");
        }
        if (users.containsKey(userDto.getId())) {
            if (userDto.getEmail() == null) {
                userDto.setEmail(users.get(id).getEmail());
                users.put(userDto.getId(), userDto);
            } else if (userDto.getName() == null) {
                userDto.setName(users.get(id).getName());
                users.put(userDto.getId(), userDto);
            } else {
                users.put(userDto.getId(), userDto);
            }
        }
        log.info("Изменения пользователя {} успешно внесены", userDto.getId());
        return userDto;
    }

    @Override
    public List<UserDto> getFindAllUsers() {
        return new ArrayList<UserDto>(users.values());
    }

    @Override
    public UserDto getUserId(Long id) {
        if (id <= 0) {
            throw new ObjectNotFoundException("Пользователь не найдет");
        }
        UserDto userDto = users.get(id);
        return userDto;
    }

    @Override
    public boolean deleteUser(Long id) {
        if (id <= 0) {
            throw new ObjectNotFoundException("Пользователь не найдет");
        }
        if (users.containsKey(id)) {
            users.remove(id);
        }
        log.info("Пользователь успешно удален");
        return true;
    }

    private User standardCheck(User user) throws ValidationException, ConflictException {
        if (user.getEmail() == null ||
                user.getEmail().isEmpty() ||
                user.getEmail().isBlank() ||
                !user.getEmail().contains("@")) {
            log.error("Неверно введен email: {}", user);
            throw new ValidationException("Неверно введен email");
        }
        for (UserDto value : users.values()) {
            if(value.getEmail().equals(user.getEmail())){
                log.error("Неверно введен email: {}", user);
                throw new ConflictException("Неверно введен email");
            }
        }
        if (user.getName().isEmpty() || user.getEmail().isBlank() || user.getName().contains(" ")) {
            log.error("Имя пользователя не может быть пустым: {}", user);
            throw new ValidationException("Имя пользователя не может быть пустым");
        }

        return user;
    }
}


