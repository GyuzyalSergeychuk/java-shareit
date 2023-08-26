package ru.practicum.shareit.request.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.storage.ItemRequestStorage;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.data.DataFactory.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {

    @Mock
    private ItemRequestStorage itemRequestStorage;

    @InjectMocks
    private ItemRequestService itemRequestService;

    @Test
    void createRequestTest() throws ValidationException {
        var userId = 1L;
        var created = LocalDateTime.now();
        var itemRequest = getItemRequest(1L,
                "Щётка для обуви",
                created,
                userId, null);
        var itemRequestDto = getItemRequestDto(1L,
                "Щётка для обуви",
                created,
                userId, null);

        when(itemRequestStorage.createRequest(userId, itemRequest)).thenReturn(itemRequestDto);

        var actualResponse = itemRequestService.createRequest(userId, itemRequest);

        assertEquals(itemRequestDto, actualResponse);
    }

    @Test
    void getAllRequestsByUserTest() throws ValidationException {
        var userId = 1L;
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);
        List<ItemDto> itemsDto = List.of(itemDto);
        var created = LocalDateTime.now();
        var itemRequestDto = getItemRequestDto(1L,
                "Щётка для обуви",
                created,
                userId, itemsDto);
        List<ItemRequestDto> itemRequestDtoList = List.of(itemRequestDto);

        when(itemRequestStorage.getAllRequestsByUser(userId)).thenReturn(itemRequestDtoList);

        var actualResponse = itemRequestService.getAllRequestsByUser(userId);

        assertEquals(itemRequestDtoList, actualResponse);
    }

    @Test
    void getAllRequestsTest() throws ValidationException {
        var userId = 1L;
        var from = 0;
        var size = 20;
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);
        List<ItemDto> itemsDto = List.of(itemDto);
        var created = LocalDateTime.now();
        var itemRequestDto = getItemRequestDto(1L,
                "Щётка для обуви",
                created,
                userId, itemsDto);
        List<ItemRequestDto> itemRequestDtoList = List.of(itemRequestDto);

        when(itemRequestStorage.getAllRequests(userId, from, size)).thenReturn(itemRequestDtoList);

        var actualResponse = itemRequestService.getAllRequests(userId, from, size);

        assertEquals(itemRequestDtoList, actualResponse);
    }

    @Test
    void getRequestsTest() throws ValidationException {
        var userId = 1L;
        var requestId = 1L;
        var created = LocalDateTime.now();
        var itemRequestDto = getItemRequestDto(1L,
                "Щётка для обуви",
                created,
                userId, null);

        when(itemRequestStorage.getRequests(userId, requestId)).thenReturn(itemRequestDto);

        var actualResponse = itemRequestService.getRequests(userId, requestId);

        assertEquals(itemRequestDto, actualResponse);
    }
}