package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

@Service
@Slf4j
public class UserClient extends BaseClient {

    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createUser(UserDto user) throws ValidationException {
        if (user.getEmail() == null ||
                user.getEmail().isEmpty() ||
                user.getEmail().isBlank() ||
                !user.getEmail().contains("@")) {
            log.error("Неверно введен email: {}", user);
            throw new ValidationException("Неверно введен email");
        }

        if (user.getName().isEmpty() || user.getEmail().isBlank() || user.getName().contains(" ")) {
            log.error("Имя пользователя не может быть пустым: {}", user);
            throw new ValidationException("Имя пользователя не может быть пустым");
        }
        return post("", user);
    }

    public ResponseEntity<Object> updateUser(Long id, UserDto user) {
        if (id <= 0) {
            throw new ObjectNotFoundException("id не может быть меньше нуля");
        }
        return patch("/", id, user);
    }

    public ResponseEntity<Object> getFindAllUsers() {
        return get("");
    }

    public ResponseEntity<Object> getUserId(Long id) {
        if (id <= 0) {
            throw new ObjectNotFoundException("id не может быть меньше нуля");
        }
        return get("/", id);
    }

    public ResponseEntity<Object> deleteUser(Long id) {
        if (id <= 0) {
            throw new ObjectNotFoundException("id не может быть меньше нуля");
        }
        return delete("/", id);
    }
}