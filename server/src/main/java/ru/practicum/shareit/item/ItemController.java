package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.services.ItemServices;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemServices itemServices;

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @RequestBody Item item) throws ValidationException {
        log.info("Получен запрос на добавление товара пользователем{}", userId);
        return itemServices.create(userId, item);
    }

    @PatchMapping("{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @PathVariable("itemId") Long itemId,
                          @RequestBody Item itemReq) {
        log.info("Получен запрос внесение изменений товара {} пользователем{}", itemId, userId);
        return itemServices.update(userId, itemId, itemReq);
    }

    @GetMapping("{itemId}")
    public ItemDto getId(@RequestHeader("X-Sharer-User-Id") Long userId,
                         @PathVariable("itemId") Long itemId) {
        log.info("Получен запрос на получение товара по номеру {} от юзера {}", itemId, userId);
        return itemServices.getItemDtoById(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> getFindAllItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestParam(name = "from", required = false) Integer from,
                                         @RequestParam(name = "size", required = false) Integer size)
            throws ValidationException {
        log.info("Получен запрос списка всех товаров пользователя{}", userId);
        return itemServices.getFindAllItems(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text,
                                    @RequestParam(name = "from", required = false) Integer from,
                                    @RequestParam(name = "size", required = false) Integer size)
            throws ValidationException {
        log.info("Получен запрос на списка товара по содержанию текста {}", text);
        return itemServices.searchItem(text, from, size);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable("itemId") Long itemId,
                                    @RequestBody Comment comment) throws ValidationException {
        log.info("Получен запрос на создание комментария пользователем{}", userId);
        return itemServices.createComment(userId, itemId, comment);
    }
}
