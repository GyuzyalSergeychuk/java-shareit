package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    ItemDto create(Long userId, Item item) throws ValidationException, ObjectNotFoundException;

    ItemDto update(Long userId, Long itemId, Item itemReq) ;

    List<ItemDto> getFindAllItemsDto(Long userId);

    List<Item> getFindAllItems(Long userId);

    ItemDto getItemDtoById(Long itemId) ;

    Item getItemById(Long itemId);

    List<ItemDto> searchItem(String text);
}
