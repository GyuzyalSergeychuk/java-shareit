package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemStorage itemStorage;

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @RequestBody Item item) throws ValidationException {
        log.info("Получен запрос на добавление товара пользователем{}", userId);
        return itemStorage.create(userId, item);
    }

    @PatchMapping("{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @PathVariable("itemId") Long itemId,
                          @RequestBody Item itemReq) {
        log.info("Получен запрос внесение изменений товара {} пользователем{}", itemId, userId);
        return itemStorage.update(userId, itemId, itemReq);
    }

    @GetMapping("{itemId}")
    public ItemDto getId(@PathVariable("itemId") Long itemId) {
        log.info("Получен запрос на получение товара по номеру {}", itemId);
        return itemStorage.getItemId(itemId);
    }

    @GetMapping
    public List<ItemDto> getFindAllItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос списка всех товаров пользователя{}", userId);
        return itemStorage.getFindAllItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        log.info("Получен запрос на списка товара по содержанию текста {}", text);
        return itemStorage.searchItem(text);
    }
}
