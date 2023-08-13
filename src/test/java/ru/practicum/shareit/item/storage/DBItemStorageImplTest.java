package ru.practicum.shareit.item.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.pagination.Pagination;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.storage.DBUserStorageImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.data.DataFactory.*;

@ExtendWith(MockitoExtension.class)
class DBItemStorageImplTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private DBUserStorageImpl userService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private Pagination<Item> pagination;
    @InjectMocks
    private DBItemStorageImpl dbItemStorage;

    @Test
    void createTest() throws ValidationException {
        var id = 1L;
        var item = getItem(1L, "Дрель", "Простая дрель", true, 1L);
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);

        when(itemRepository.save(item)).thenReturn(item);
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        var actualResponse = dbItemStorage.create(id, item);

        assertEquals(itemDto, actualResponse);
    }

    @Test
    void createNameNullTest() {
        var id = 1L;
        var item = getItem(1L, null, "Простая дрель", true, 1L);

        assertThrows(ValidationException.class,
                () -> dbItemStorage.create(id, item),
                "Неверно указано название вещи");
    }

    @Test
    void createDescriptionNullTest() {
        var id = 1L;
        var item = getItem(1L, "Дрель", null, true, 1L);

        assertThrows(ValidationException.class,
                () -> dbItemStorage.create(id, item),
                "Неверно указано описание вещи");
    }

    @Test
    void createAvailableNullTest() {
        var id = 1L;
        var item = getItem(1L, "Дрель", "Простая дрель", null, 1L);

        assertThrows(ValidationException.class,
                () -> dbItemStorage.create(id, item),
                "Неверно указано available вещи");
    }

    @Test
    void updateTest() {
        var userId = 1L;
        var itemId = 1L;
        var item = getItem(1L, "Дрель", "Простая дрель", true, 1L);
        var itemUpdate = getItem(1L, "Дрель", "Простая дрель", false, 1L);
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", false, 1L);

        when(itemRepository.findByIdSelf(itemId, userId)).thenReturn(item);
        when(itemRepository.save(item)).thenReturn(itemUpdate);
        when(itemMapper.toItemDto(itemUpdate)).thenReturn(itemDto);

        var actualResponse = dbItemStorage.update(userId, itemId, item);

        assertEquals(itemDto, actualResponse);
    }

    @Test
    void updateId0Test() {
        var userId = 1L;
        var itemId = 0L;
        var item = getItem(0L, "Дрель", "Простая дрель", true, 1L);

        assertThrows(ObjectNotFoundException.class,
                () -> dbItemStorage.update(userId, itemId, item),
                "Вещь не найдена");
    }

    @Test
    void updateUserId99Test() {
        var userId = 99L;
        var itemId = 1L;
        var item = getItem(1L, "Дрель", "Простая дрель", true, 1L);

        when(itemRepository.findByIdSelf(itemId, userId)).thenReturn(null);

        assertThrows(ObjectNotFoundException.class,
                () -> dbItemStorage.update(userId, itemId, item),
                "Юзер пытается редактировать чужой товар");
    }

    @Test
    void updateNameNullTest() {
        var userId = 1L;
        var itemId = 1L;
        var item = getItem(1L, "Дрель", null, null, 1L);
        var item1 = getItem(1L, "Дрель", "Простая дрель", false, 1L);
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", false, 1L);

        when(itemRepository.findByIdSelf(itemId, userId)).thenReturn(item1);
        when(itemRepository.save(item1)).thenReturn(item1);
        when(itemMapper.toItemDto(item1)).thenReturn(itemDto);

        var actualResponse = dbItemStorage.update(userId, itemId, item);

        assertEquals(itemDto, actualResponse);
    }

    @Test
    void updateDescriptionNullTest() {
        var userId = 1L;
        var itemId = 1L;
        var item = getItem(1L, null, "Простая дрель", null, 1L);
        var item1 = getItem(1L, "Дрель", "Простая дрель", false, 1L);
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", false, 1L);

        when(itemRepository.findByIdSelf(itemId, userId)).thenReturn(item1);
        when(itemRepository.save(item1)).thenReturn(item1);
        when(itemMapper.toItemDto(item1)).thenReturn(itemDto);

        var actualResponse = dbItemStorage.update(userId, itemId, item);

        assertEquals(itemDto, actualResponse);
    }

    @Test
    void getAllItemsDtoTest() throws ValidationException {
        var userId = 1L;
        Integer from = null;
        Integer size = null;
        var item = getItem(1L, "Дрель", "Простая дрель", false, 1L);
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", false, 1L);
        List<Item> itemList = List.of(item);
        List<ItemDto> itemDtoList = List.of(itemDto);

        when(itemRepository.findByOwnerIdOrderByNextBookingIdDesc(userId)).thenReturn(itemList);
        when(pagination.makePagination(0, 20, itemList)).thenReturn(itemList);
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        var actualResponse = dbItemStorage.getAllItemsDto(userId, from, size);

        assertEquals(itemDtoList, actualResponse);
    }

    @Test
    void getAllItemsSize20DtoTest() throws ValidationException {
        var userId = 1L;
        Integer from = 0;
        Integer size = 20;
        var item = getItem(1L, "Дрель", "Простая дрель", false, 1L);
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", false, 1L);
        List<Item> itemList = List.of(item);
        List<ItemDto> itemDtoList = List.of(itemDto);

        when(itemRepository.findByOwnerIdOrderByNextBookingIdDesc(userId)).thenReturn(itemList);
        when(pagination.makePagination(0, 20, itemList)).thenReturn(itemList);
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        var actualResponse = dbItemStorage.getAllItemsDto(userId, from, size);

        assertEquals(itemDtoList, actualResponse);
    }

    @Test
    void getAllItemsSizeMinus1DtoTest() {
        var userId = 1L;
        var item = getItem(1L, "Дрель", "Простая дрель", false, 1L);
        List<Item> itemList = List.of(item);

        when(itemRepository.findByOwnerIdOrderByNextBookingIdDesc(userId)).thenReturn(itemList);

        assertThrows(ValidationException.class,
                () -> dbItemStorage.getAllItemsDto(userId, -1, -1),
                "from and size не могут быть меньше нулями");
    }

    @Test
    void getAllItemsTest() {
        var userId = 1L;
        var item = getItem(1L, "Дрель", "Простая дрель", false, 1L);
        List<Item> itemList = List.of(item);

        when(itemRepository.findByOwnerId(userId)).thenReturn(itemList);

        var actualResponse = dbItemStorage.getAllItems(userId);

        assertEquals(itemList, actualResponse);
    }

    @Test
    void getItemDtoByIdTest() {
        var userId = 1L;
        var itemId = 1L;
        var item = getItem(1L, "Дрель", "Простая дрель", true, 4L);
        var item1 = getItem(1L, "Дрель", "Простая дрель", true, 4L);
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 4L);

        when(itemRepository.findById(userId)).thenReturn(Optional.of(item));
        when(itemMapper.toItemDto(item1)).thenReturn(itemDto);

        var actualResponse = dbItemStorage.getItemDtoById(userId, itemId);

        assertEquals(itemDto, actualResponse);
    }

    @Test
    void getItemDtoByOwnerEqualsUserIdTest() {
        var userId = 1L;
        var itemId = 1L;
        var item = getItem(1L, "Дрель", "Простая дрель", true, 1L);
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);
        var start = LocalDateTime.now().plusMinutes(10);
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, itemId, 2L, Status.APPROVED, 4L);
        var bookingList = List.of(booking);
        var bookingForGetItemDto = getBookingForGetItemDto(1L, 2L);
        var expectedItemDto = getItemDtoBooking(1L, "Дрель", "Простая дрель", true,
                1L, bookingForGetItemDto, bookingForGetItemDto, null, null);

        when(itemRepository.findById(userId)).thenReturn(Optional.of(item));
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);
        when(bookingRepository.findByItemIdOrderByStartDesc(itemDto.getId()))
                .thenReturn(bookingList);
        when(bookingRepository.getById(booking.getId())).thenReturn(booking);

        var actualResponse = dbItemStorage.getItemDtoById(userId, itemId);

        assertEquals(expectedItemDto, actualResponse);
    }

    @Test
    void getItemDtoId0Test() {
        var userId = 1L;
        var itemId = 0L;

        assertThrows(ObjectNotFoundException.class,
                () -> dbItemStorage.getItemDtoById(userId, itemId),
                "Id не может быть меньше нуля");
    }

    @Test
    void getItemDtoItemId99Test() {
        var userId = 1L;
        var itemId = 99L;

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class,
                () -> dbItemStorage.getItemDtoById(userId, itemId),
                "Вещь не найдена");
    }

    @Test
    void searchItemTest() throws ValidationException {
        var text = "Простая дрель";
        Integer from = null;
        Integer size = null;
        var item = getItem(1L, "Дрель", "Простая дрель", true, 4L);
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 4L);
        List<Item> itemList = List.of(item);
        List<ItemDto> itemDtoList = List.of(itemDto);

        when(itemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text))
                .thenReturn(itemList);
        when(pagination.makePagination(0, 20, itemList)).thenReturn(itemList);
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        var actualResponse = dbItemStorage.searchItem(text, from, size);

        assertEquals(itemDtoList, actualResponse);
    }

    @Test
    void searchItemSizeMinus1Test() {
        var text = "Простая дрель";
        Integer from = 0;
        Integer size = -1;
        var item = getItem(1L, "Дрель", "Простая дрель", true, 4L);
        List<Item> itemList = List.of(item);

        when(itemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text))
                .thenReturn(itemList);

        assertThrows(ValidationException.class,
                () -> dbItemStorage.searchItem(text, from, size),
                "from and size не могут быть нулями");
    }

    @Test
    void searchItemSize20Test() throws ValidationException {
        var text = "Простая дрель";
        Integer from = 0;
        Integer size = 20;
        var item = getItem(1L, "Дрель", "Простая дрель", true, 4L);
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 4L);
        List<Item> itemList = List.of(item);
        List<ItemDto> itemDtoList = List.of(itemDto);

        when(itemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text))
                .thenReturn(itemList);
        when(pagination.makePagination(0, 20, itemList)).thenReturn(itemList);
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        var actualResponse = dbItemStorage.searchItem(text, from, size);

        assertEquals(itemDtoList, actualResponse);
    }

    @Test
    void searchItemTextNullTest() {
        String text = null;
        Integer from = 0;
        Integer size = 20;

        assertThrows(ObjectNotFoundException.class,
                () -> dbItemStorage.searchItem(text, from, size),
                "Запрос на поиск товара не может быть пустым");
    }

    @Test
    void searchItemTextTest() throws ValidationException {
        String text = " ";
        Integer from = 0;
        Integer size = 20;
        List<Item> itemList = List.of();

        var actualResponse = dbItemStorage.searchItem(text, from, size);

        assertEquals(itemList, actualResponse);
    }

    @Test
    void createCommentTest() throws ValidationException {
        var userId = 1L;
        var itemId = 1L;
        var created = LocalDateTime.now();
        var start = LocalDateTime.now().minusHours(5);
        var end = LocalDateTime.now().minusHours(4);
        var item = getItem(1L, "Дрель", "Простая дрель", true, 4L);
        var user = getUser(1L, "user", "user@user.com");
        var comment = getComment(1L, "Кухонный стол", userId, created, itemId);
        var commentDto = getCommentDto(1L, "Кухонный стол", "user", created);
        var booking = getBooking(userId, start, end, itemId, userId, Status.APPROVED, 4L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.findById(userId)).thenReturn(Optional.of(booking));
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.toCommentDto(comment)).thenReturn(commentDto);

        var actualResponse = dbItemStorage.createComment(userId, itemId, comment);

        assertEquals(commentDto, actualResponse);
    }

    @Test
    void createCommentTextNullTest() {
        var userId = 1L;
        var itemId = 1L;
        var created = LocalDateTime.now();
        var start = LocalDateTime.now().minusHours(5);
        var end = LocalDateTime.now().minusHours(4);
        var item = getItem(1L, "Дрель", "Простая дрель", true, 4L);
        var user = getUser(1L, "user", "user@user.com");
        var comment = getComment(1L, null, userId, created, itemId);
        var booking = getBooking(1L, start, end, itemId, userId, Status.APPROVED, 4L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.findById(userId)).thenReturn(Optional.of(booking));

        assertThrows(ValidationException.class,
                () -> dbItemStorage.createComment(userId, itemId, comment),
                "Текст комментария не может быть пустым");
    }

    @Test
    void createCommentOwnerIdEqualsItemIdTest() {
        var userId = 1L;
        var itemId = 1L;
        var created = LocalDateTime.now();
        var start = LocalDateTime.now().minusHours(5);
        var end = LocalDateTime.now().minusHours(4);
        var item = getItem(1L, "Дрель", "Простая дрель", true, 4L);
        var user = getUser(1L, "user", "user@user.com");
        var comment = getComment(1L, "Дрель", userId, created, itemId);
        var booking = getBooking(1L, start, end, itemId, 4L, Status.APPROVED, 4L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.findById(userId)).thenReturn(Optional.of(booking));

        assertThrows(ValidationException.class,
                () -> dbItemStorage.createComment(userId, itemId, comment),
                "Не возможно добавить комментарий");
    }
}