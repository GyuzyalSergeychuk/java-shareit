package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(Long userId, ItemDto itemDto) {
        if (userId <= 0) {
            throw new ObjectNotFoundException("Id не может быть меньше нуля");
        }
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> updateItem(Long userId, Long itemId, ItemDto itemReq) {
        if (itemId <= 0) {
            throw new ObjectNotFoundException("Id не может быть меньше нуля");
        }

        if (userId <= 0) {
            throw new ObjectNotFoundException("Id не может быть меньше нуля");
        }
        return patch("/" + itemId, userId, itemReq);
    }

    public ResponseEntity<Object> getId(Long userId, Long itemId) {
        if (itemId <= 0) {
            throw new ObjectNotFoundException("Id не может быть меньше нуля");
        }

        if (userId <= 0) {
            throw new ObjectNotFoundException("Id не может быть меньше нуля");
        }
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getFindAllItems(Long userId, Integer from, Integer size) throws ValidationException {
        if (from < 0 || size < 0 || size == 0) {
            throw new ValidationException("Индекс первого элемента и размер листа не может быть меньше нуля");
        }
        String path = "?from=" + from;
        if (size != null) {
            path += "&size=" + size;
        }
        return get(path, userId);
    }

    public ResponseEntity<Object> searchItem(String text, Integer from, Integer size) throws ValidationException {
        if (from < 0 || size < 0 || size == 0) {
            throw new ValidationException("Индекс первого элемента и размер листа не может быть меньше нуля");
        }
        if (text == null) {
            throw new ObjectNotFoundException("Запрос на поиск товара не может быть пустым");
        }
        if (text.isEmpty() || text.isBlank()) {
            return new ResponseEntity<>(List.of(), HttpStatus.OK);
        }
        String path = "/search?text=" + text + "&from=" + from;
        if (size != null) {
            path += "&size=" + size;
        }
        return get(path);
    }

    public ResponseEntity<Object> createComment(Long userId, Long itemId, CommentDto commentDto) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }
}
