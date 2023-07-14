package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.List;
import java.util.Optional;

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
    return itemStorage.create(userId, item);
    }

    @PatchMapping("{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @RequestBody Item itemReq){
        return itemStorage.update(userId, itemReq);
    }

    @GetMapping
    public List<ItemDto> getFindAllItems() {
        return itemStorage.getFindAllItems();
    }

    @GetMapping("{id}")
    public List<Item> getItemId(@PathVariable("id") Long id){
        return itemStorage.getItemId(id);
    }

    @GetMapping("{search}")
    public List<ItemDto> itemsAreAvailable(@RequestParam String text){
        return itemStorage.itemsAreAvailable(text);
    }
}
