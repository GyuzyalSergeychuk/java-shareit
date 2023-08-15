package ru.practicum.shareit.item.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.data.DataFactory.*;

@ExtendWith(MockitoExtension.class)
class ItemServicesTest {

    @Mock
    private ItemStorage itemStorage;

    @InjectMocks
    private ItemServices itemServices;

    @Test
    void createTest() throws ValidationException {
        var userId = 1L;
        var item = getItem(1L, "Дрель", "Простая дрель", null, 1L);
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", null, 1L);

        when(itemServices.create(userId, item)).thenReturn(itemDto);

        var actualResponse = itemStorage.create(userId, item);

        assertEquals(itemDto, actualResponse);
    }

    @Test
    void updateTest() {
        var userId = 1L;
        var itemId = 1L;
        var item = getItem(1L, "Дрель", "Простая дрель", null, 1L);
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", null, 1L);

        when(itemServices.update(userId, itemId, item)).thenReturn(itemDto);

        var actualResponse = itemStorage.update(userId, itemId, item);

        assertEquals(itemDto, actualResponse);
    }

    @Test
    void getItemDtoByIdTest() {
        var userId = 1L;
        var itemId = 1L;
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", null, 1L);

        when(itemServices.getItemDtoById(userId, itemId)).thenReturn(itemDto);

        var actualResponse = itemStorage.getItemDtoById(userId, itemId);

        assertEquals(itemDto, actualResponse);
    }

    @Test
    void getFindAllItemsTest() throws ValidationException {
        var userId = 1L;
        var from = 0;
        var size = 20;
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 4L);
        List<ItemDto> itemDtoList = List.of(itemDto);

        when(itemServices.getFindAllItems(userId, from, size)).thenReturn(itemDtoList);

        var actualResponse = itemStorage.getAllItemsDto(userId, from, size);

        assertEquals(itemDtoList, actualResponse);
    }

    @Test
    void searchItemTest() throws ValidationException {
        var text = "Простая дрель";
        var from = 0;
        var size = 20;
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 4L);
        List<ItemDto> itemDtoList = List.of(itemDto);

        when(itemServices.searchItem(text, from, size)).thenReturn(itemDtoList);

        var actualResponse = itemStorage.searchItem(text, from, size);

        assertEquals(itemDtoList, actualResponse);
    }

    @Test
    void createCommentTest() throws ValidationException {
        var userId = 1L;
        var itemId = 1L;
        var created = LocalDateTime.now();
        var comment = getComment(1L, "Кухонный стол", userId, created, itemId);
        var commentDto = getCommentDto(1L, "Кухонный стол", "user", created);

        when(itemServices.createComment(userId, itemId, comment)).thenReturn(commentDto);

        var actualResponse = itemStorage.createComment(userId, itemId, comment);

        assertEquals(commentDto, actualResponse);

    }
}