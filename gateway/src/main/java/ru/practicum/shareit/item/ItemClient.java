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
import ru.practicum.shareit.item.dto.CommentRequestDto;
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
        return patch("/" + itemId, userId, itemReq);
    }

    public ResponseEntity<Object> getId(Long userId, Long itemId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getFindAllItems(Long userId, Integer from, Integer size) {
        return get("/", userId);
    }

    public ResponseEntity<Object> searchItem(String text, Integer from, Integer size)  {
        String puth = "/search?text=" + text;
        if(size != null){
            puth = "/search?text=" + text + "&from=" + from + "&size=" + size;
        }
        return get(puth);
    }

    public ResponseEntity<Object> createComment(Long userId, Long itemId, CommentRequestDto comment) {
        log.info("запрос на создание комментария ={}", comment);
        return post("/" + itemId + "/comment", userId, comment);
    }
}
