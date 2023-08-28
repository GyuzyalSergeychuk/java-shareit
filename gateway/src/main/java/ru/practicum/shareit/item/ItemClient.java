package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

@Service
@Slf4j
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

    public ResponseEntity<Object> createItem(Long userId, ItemRequestDto item) {
        return post("", userId, item);
    }

    public ResponseEntity<Object> updateItem(Long userId, Long itemId, ItemRequestDto itemReq) {
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

    public ResponseEntity<Object> getFindAllItems(Long userId, Integer from, Integer size) {
        return get("/", userId);
    }

    public ResponseEntity<Object> searchItem(Long userId, String text, Integer from, Integer size)  {
        return get("/search?text=" + text);
    }

    public ResponseEntity<Object> createComment(Long userId, Long itemId, CommentDto commentDto) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }
}
