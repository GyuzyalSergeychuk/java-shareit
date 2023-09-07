package ru.practicum.shareit.request.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.data.DataFactory.*;

@ExtendWith(MockitoExtension.class)
class DBItemRequestStorageImplTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private ItemRequestMapper itemRequestMapper;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DBItemRequestStorageImpl dbItemRequestStorage;

    @Test
    void createRequestTest() throws ValidationException {
        var userId = 1L;
        var user = getUser(1L, "user", "user@user.com");
        var item = getItem(1L, "Дрель", "Простая дрель", true, 1L);
        List<Item> items = List.of(item);
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);
        List<ItemDto> itemsDto = List.of(itemDto);
        var created = LocalDateTime.now();
        var itemRequest = getItemRequest(1L,
                "Щётка для обуви",
                created,
                userId, items);
        var itemRequestDto = getItemRequestDto(1L,
                "Щётка для обуви",
                created,
                userId, itemsDto);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(itemRequest)).thenReturn(itemRequest);
        when(itemRequestMapper.toItemRequestDto(itemRequest)).thenReturn(itemRequestDto);

        var actualResponse = dbItemRequestStorage.createRequest(userId, itemRequest);

        assertEquals(itemRequestDto, actualResponse);
    }

    @Test
    void createRequestUserId99Test() {
        var userId = 99L;
        var item = getItem(1L, "Дрель", "Простая дрель", true, 1L);
        List<Item> items = List.of(item);
        var created = LocalDateTime.now();
        var itemRequest = getItemRequest(1L,
                "Щётка для обуви",
                created,
                userId, items);

        assertThrows(ObjectNotFoundException.class,
                () -> dbItemRequestStorage.createRequest(userId, itemRequest),
                "Пользователь не найден");
    }

    @Test
    void createRequestDescriptionIsBlankTest() {
        var userId = 99L;
        var item = getItem(1L, "Дрель", "Простая дрель", true, 1L);
        List<Item> items = List.of(item);
        var created = LocalDateTime.now();
        var itemRequest = getItemRequest(1L,
                " ",
                created,
                userId, items);

        assertThrows(ValidationException.class,
                () -> dbItemRequestStorage.createRequest(userId, itemRequest),
                "Описание запроса отсутствует");
    }

    @Test
    void getAllRequestsByUserTest() throws ValidationException {
        var userId = 1L;
        var user = getUser(1L, "user", "user@user.com");
        var item = getItem(1L, "Дрель", "Простая дрель", true, 1L);
        List<Item> items = List.of(item);
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);
        List<ItemDto> itemsDto = List.of(itemDto);
        var created = LocalDateTime.now();
        var itemRequest = getItemRequest(1L,
                "Щётка для обуви",
                created,
                userId, items);
        var itemRequestList = List.of(itemRequest);
        var itemRequestDto = getItemRequestDto(1L,
                "Щётка для обуви",
                created,
                userId, itemsDto);
        List<ItemRequestDto> itemRequestDtoList = List.of(itemRequestDto);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findAllByUserId(userId)).thenReturn(itemRequestList);
        when(itemRequestMapper.toItemRequestDto(itemRequest)).thenReturn(itemRequestDto);

        var actualResponse = dbItemRequestStorage.getAllRequestsByUser(userId);

        assertEquals(itemRequestDtoList, actualResponse);
    }

    @Test
    void getAllRequestsFromMinusTest() {
        var userId = 1L;
        var user = getUser(1L, "user", "user@user.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(ValidationException.class,
                () -> dbItemRequestStorage.getAllRequests(userId, -1, 0),
                "Индекс первого элемента и размер листа не может быть меньше нуля");
    }

    @Test
    void getAllRequestsTest() throws ValidationException {
        var userId = 1L;
        var userId2 = 2L;
        var from = 0;
        var size = 20;
        var user = getUser(1L, "user", "user@user.com");
        var item = getItem(1L, "Дрель", "Простая дрель", true, 1L);
        List<Item> items = List.of(item);
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);
        List<ItemDto> itemsDto = List.of(itemDto);
        var created = LocalDateTime.now();
        var itemRequest = getItemRequest(1L,
                "Щётка для обуви",
                created,
                userId2, items);
        var itemRequestList = List.of(itemRequest);
        var itemRequestDto = getItemRequestDto(1L,
                "Щётка для обуви",
                created,
                userId2, itemsDto);
        List<ItemRequestDto> itemRequestDtoList = List.of(itemRequestDto);
        Page<ItemRequest> request = new PageImpl<>(itemRequestList);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findAll(PageRequest.of(from, size, Sort.by("created").descending())))
                .thenReturn(request);
        when(itemRequestMapper.toItemRequestDto(itemRequest)).thenReturn(itemRequestDto);

        var actualResponse = dbItemRequestStorage.getAllRequests(userId, from, size);

        assertEquals(itemRequestDtoList, actualResponse);
    }

    @Test
    void getRequestsTest() throws ValidationException {
        var userId = 1L;
        var itemRequestId = 1L;
        var user = getUser(1L, "user", "user@user.com");
        var item = getItem(1L, "Дрель", "Простая дрель", true, 1L);
        List<Item> items = List.of(item);
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);
        List<ItemDto> itemsDto = List.of(itemDto);
        var created = LocalDateTime.now();
        var itemRequest = getItemRequest(1L,
                "Щётка для обуви",
                created,
                userId, items);
        var itemRequestDto = getItemRequestDto(1L,
                "Щётка для обуви",
                created,
                userId, itemsDto);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(itemRequestId)).thenReturn(Optional.of(itemRequest));
        when(itemRequestMapper.toItemRequestDto(itemRequest)).thenReturn(itemRequestDto);

        var actualResponse = dbItemRequestStorage.getRequests(userId, itemRequestId);

        assertEquals(itemRequestDto, actualResponse);
    }

    @Test
    void getRequestsIdMinusTest() {
        var userId = 1L;
        var requestId = -1L;
        var user = getUser(1L, "user", "user@user.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(ValidationException.class,
                () -> dbItemRequestStorage.getRequests(userId, requestId),
                "Значение requestId не может быть меньше нуля");
    }

    @Test
    void getRequestsId99Test() {
        var userId = 1L;
        var requestId = 99L;
        var user = getUser(1L, "user", "user@user.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(ObjectNotFoundException.class,
                () -> dbItemRequestStorage.getRequests(userId, requestId),
                "Пользователь не найден");
    }
}