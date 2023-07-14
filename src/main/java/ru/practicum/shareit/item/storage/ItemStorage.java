package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    ItemDto create(Long userId, Item item) throws ValidationException, ObjectNotFoundException;

    ItemDto update(Long userId, Item itemReq);

    List<ItemDto> getFindAllItems();

    List<Item> getItemId(Long id);

    List<ItemDto> itemsAreAvailable(String description);
}
