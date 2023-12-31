package ru.practicum.shareit.item.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServices {

    private final ItemStorage itemStorage;

    public ItemDto create(Long userId, Item item) throws ValidationException {
        return itemStorage.create(userId, item);
    }

    public ItemDto update(Long userId, Long itemId, Item itemReq) {
        return itemStorage.update(userId, itemId, itemReq);
    }

    public ItemDto getItemDtoById(Long userId, Long itemId) {
        return itemStorage.getItemDtoById(userId, itemId);
    }

    public List<ItemDto> getFindAllItems(Long userId, Integer from, Integer size) throws ValidationException {
        return itemStorage.getAllItemsDto(userId, from, size);
    }

    public List<ItemDto> searchItem(String text, Integer from, Integer size) throws ValidationException {
        return itemStorage.searchItem(text, from, size);
    }

    public CommentDto createComment(Long userId, Long itemId, Comment comment) throws ValidationException {
        return itemStorage.createComment(userId, itemId, comment);
    }
}
