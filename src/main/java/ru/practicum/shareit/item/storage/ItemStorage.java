package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;

import java.util.List;

public interface ItemStorage {

    ItemDto create(Long userId, Item item) throws ValidationException, ObjectNotFoundException;

    ItemDto update(Long userId, Long itemId, Item itemReq);

    List<ItemDto> getAllItemsDto(Long userId);

    List<Item> getAllItems(Long userId);

    ItemDto getItemDtoById(Long userId, Long itemId);

    List<ItemDto> searchItem(String text);

    CommentDto createComment(Long userId, Long itemId, Comment comment) throws ValidationException;
}
