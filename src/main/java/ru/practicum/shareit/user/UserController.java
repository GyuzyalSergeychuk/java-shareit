package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserStorage userStorage;

    @PostMapping
    public UserDto create(@RequestBody User user) throws ValidationException, ConflictException {
        log.info("Получен запрос на добавление пользователя");
        return userStorage.create(user);
    }

    @PatchMapping("{userId}")
    public UserDto update(@PathVariable("userId") Long id, @RequestBody User user) throws ValidationException, ConflictException {
        log.info("Получен запрос на внесение изменений {}", user);
        return userStorage.update(id, user);
    }

    @GetMapping
    public List<UserDto> getFindAllUsers() {
        log.info("Получен запрос на получение всех пользователей");
        return userStorage.getFindAllUsers();
    }

    @GetMapping("{id}")
    public UserDto getUserId(@PathVariable("id") Long id) {
        log.info("Получен запрос на получение пользователя {}", id);
        return userStorage.getUserId(id);
    }

    @DeleteMapping("{id}")
    public boolean deleteUser(@PathVariable("id") Long id) {
        log.info("Получен запрос на удаление пользователя {}", id);
        return userStorage.deleteUser(id);
    }
}
