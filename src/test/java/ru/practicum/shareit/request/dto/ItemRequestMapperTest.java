package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.data.DataFactory.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestMapperTest {

    @Mock
    private ItemMapper itemMapper;
    @InjectMocks
    private ItemRequestMapper itemRequestMapper;

    @Test
    void toItemRequestDtoTest() {
        var item = new Item();
        List<Item> items = List.of(item);
        var itemDto = ItemDto.builder().build();
        List<ItemDto> itemsDto = List.of(itemDto);
        var created = LocalDateTime.now();
        var itemRequest = getItemRequest(1L, "Щётка для обуви", created, 1L, items);
        var itemRequestDto = getItemRequestDto(1L, "Щётка для обуви", created, null, itemsDto);

        when(itemMapper.toItemDto(item)).thenReturn(itemDto);
        var actualResp = itemRequestMapper.toItemRequestDto(itemRequest);

        assertEquals(itemRequestDto, actualResp);
    }

    @Test
    void toItemRequestDtoItemNullTest() {
        List<ItemDto> itemsDto = new ArrayList<>();
        var created = LocalDateTime.now();
        var itemRequest = getItemRequest(1L,
                "Щётка для обуви",
                created,
                1L,
                null);
        var itemRequestDto = getItemRequestDto(1L,
                "Щётка для обуви",
                created,
                null,
                itemsDto);

        var actualResp = itemRequestMapper.toItemRequestDto(itemRequest);

        assertEquals(itemRequestDto, actualResp);
    }
}