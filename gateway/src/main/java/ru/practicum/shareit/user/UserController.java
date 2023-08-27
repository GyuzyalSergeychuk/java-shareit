package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UserDto user) throws ValidationException {
        log.info("Получен запрос на добавление пользователя");
        return userClient.createUser(user);
    }

    @PatchMapping("{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable("userId") Long id, @RequestBody UserDto user) {
        log.info("Получен запрос на внесение изменений {}", user);
        return userClient.updateUser(id, user);
    }

    @GetMapping
    public ResponseEntity<Object> getFindAllUsers() {
        log.info("Получен запрос на получение всех пользователей");
        return userClient.getFindAllUsers();
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getUserId(@PathVariable("id") Long id) {
        log.info("Получен запрос на получение пользователя {}", id);
        return userClient.getUserId(id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") Long id) {
        log.info("Получен запрос на удаление пользователя {}", id);
        return userClient.deleteUser(id);
    }
}
