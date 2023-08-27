package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestBody ItemDto item) {
        log.info("Получен запрос на добавление товара пользователем{}", userId);
        return itemClient.createItem(userId, item);
    }

    @PatchMapping("{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable("itemId") Long itemId,
                                             @RequestBody ItemDto itemReq) {
        log.info("Получен запрос внесение изменений товара {} пользователем{}", itemId, userId);
        return itemClient.updateItem(userId, itemId, itemReq);
    }

    @GetMapping("{itemId}")
    public ResponseEntity<Object> getId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @PathVariable("itemId") Long itemId) {
        log.info("Получен запрос на получение товара по номеру {} от юзера {}", itemId, userId);
        return itemClient.getId(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getFindAllItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(name = "from", required = false) Integer from,
                                                  @RequestParam(name = "size", required = false) Integer size)
            throws ValidationException {
        log.info("Получен запрос списка всех товаров пользователя{}", userId);
        return itemClient.getFindAllItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestParam String text,
                                             @RequestParam(name = "from", required = false) Integer from,
                                             @RequestParam(name = "size", required = false) Integer size)
            throws ValidationException {
        log.info("Получен запрос на списка товара по содержанию текста {}", text);
        return itemClient.searchItem(text, from, size);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PathVariable("itemId") Long itemId,
                                                @RequestBody CommentDto comment) throws ValidationException {
        log.info("Получен запрос на создание комментария пользователем{}", userId);
        return itemClient.createComment(userId, itemId, comment);
    }
}
