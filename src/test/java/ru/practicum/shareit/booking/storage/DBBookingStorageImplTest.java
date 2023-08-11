package ru.practicum.shareit.booking.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.storage.DBItemStorageImpl;
import ru.practicum.shareit.pagination.Pagination;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.storage.DBUserStorageImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.data.DataFactory.*;

@ExtendWith(MockitoExtension.class)
class DBBookingStorageImplTest {

    @Mock
    private BookingMapper bookingMapper;
    @Mock
    private DBUserStorageImpl dbUserStorage;
    @Mock
    private DBItemStorageImpl dbItemStorage;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private Pagination<Booking> pagination;
    @Mock
    private Status status;
    @InjectMocks
    private DBBookingStorageImpl dbBookingStorage;

    @Test
    void createTest() throws ValidationException {
        var userId = 1L;
        var itemId = 1L;
        var item = getItem(1L, "Дрель", "Простая дрель", true, 4L);
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 4L);
        var user = getUser(1L, "user", "user@user.com");
        var userDto = getUserDto(1L, "user", "user@user.com");
        var start = LocalDateTime.now().plusMinutes(10);
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, itemId, userId, Status.WAITING, 4L);
        var bookingDto = getBookingDto(1L, start, end, itemDto, userDto, Status.APPROVED, 4L);
        List<Booking> bookingList = List.of(booking);

        when(dbUserStorage.getUserById(userId)).thenReturn(user);
        when(itemRepository.findById(booking.getItemId())).thenReturn(Optional.of(item));
        when(bookingRepository.findByItemIdOrderByStartDesc(itemId)).thenReturn(bookingList);
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        var actualResponse = dbBookingStorage.create(userId, booking);

        assertEquals(bookingDto, actualResponse);
    }

    @Test
    void createAvailableFalseTest() {
        var userId = 1L;
        var itemId = 1L;
        var item = getItem(1L, "Дрель", "Простая дрель", false, 4L);
        var user = getUser(1L, "user", "user@user.com");
        var start = LocalDateTime.now().plusMinutes(10);
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, itemId, userId, Status.WAITING, 4L);
        List<Booking> bookingList = List.of(booking);

        when(dbUserStorage.getUserById(userId)).thenReturn(user);
        when(itemRepository.findById(booking.getItemId())).thenReturn(Optional.of(item));

        assertThrows(ValidationException.class,
                ()-> dbBookingStorage.create(userId, booking),
        "Вещь недоступна для бронирования");
    }

    @Test
    void createItemNullTest() {
        var userId = 1L;
        var itemId = 1L;
        var item = getItem(1L, "Дрель", "Простая дрель", false, 4L);
        var user = getUser(1L, "user", "user@user.com");
        var start = LocalDateTime.now();
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, itemId, userId, Status.WAITING, 4L);
        List<Booking> bookingList = List.of(booking);

        when(dbUserStorage.getUserById(userId)).thenReturn(user);
        when(itemRepository.findById(booking.getItemId())).thenReturn(Optional.of(item));

        assertThrows(ValidationException.class,
                ()-> dbBookingStorage.create(userId, booking),
                "Неверно указана дата бронирования");
    }

    @Test
    void createOwnerIdEqualsUserIdTest() {
        var userId = 4L;
        var itemId = 1L;
        var item = getItem(1L, "Дрель", "Простая дрель", true, 4L);
        var user = getUser(1L, "user", "user@user.com");
        var start = LocalDateTime.now().plusMinutes(10);
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, itemId, userId, Status.WAITING, 4L);
        List<Booking> bookingList = List.of(booking);

        when(dbUserStorage.getUserById(userId)).thenReturn(user);
        when(itemRepository.findById(booking.getItemId())).thenReturn(Optional.of(item));

        assertThrows(ObjectNotFoundException.class,
                ()-> dbBookingStorage.create(userId, booking),
                "Владелец вещи пытается сделать бронирование своей вещи");
    }

    @Test
    void createStatusApprovedTest() {
        var userId = 1L;
        var itemId = 1L;
        var item = getItem(1L, "Дрель", "Простая дрель", true, 4L);
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 4L);
        var user = getUser(1L, "user", "user@user.com");
        var userDto = getUserDto(1L, "user", "user@user.com");
        var start = LocalDateTime.now().plusMinutes(10);
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, itemId, userId, Status.APPROVED, 4L);
        var bookingDto = getBookingDto(1L, start, end, itemDto, userDto, Status.APPROVED, 4L);
        List<Booking> bookingList = List.of(booking);

        when(dbUserStorage.getUserById(userId)).thenReturn(user);
        when(itemRepository.findById(booking.getItemId())).thenReturn(Optional.of(item));
        when(bookingRepository.findByItemIdOrderByStartDesc(itemId)).thenReturn(bookingList);

        assertThrows(ObjectNotFoundException.class,
                ()-> dbBookingStorage.create(userId, booking),
                "Предмет нельзя забронировать. Он уже используется кем-то!");
    }

    @Test
    void createStartNullTest() {
        var userId = 1L;
        var itemId = 1L;
        var item = getItem(1L, "Дрель", "Простая дрель", true, 4L);
        var user = getUser(1L, "user", "user@user.com");
        LocalDateTime start = null;
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, itemId, userId, Status.APPROVED, 4L);

        when(dbUserStorage.getUserById(userId)).thenReturn(user);
        when(itemRepository.findById(booking.getItemId())).thenReturn(Optional.of(item));

        assertThrows(ValidationException.class,
                ()-> dbBookingStorage.create(userId, booking),
                "Неверно указана дата бронирования");
    }

    @Test
    void approvedTest() throws ValidationException {
        var userId = 1L;
        var itemId = 1L;
        var bookingId = 1l;
        var user = getUser(1L, "user", "user@user.com");
        var userDto = getUserDto(2L, "user", "user@user.com");
        var item = getItem(1L, "Дрель", "Простая дрель", true, 1L);
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);
        var start = LocalDateTime.now().plusMinutes(10);
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, itemId, 2L, Status.WAITING, 4L);
        var bookingDto = getBookingDto(1L, start, end, itemDto, userDto, Status.WAITING, 4L);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(itemRepository.findById(booking.getItemId())).thenReturn(Optional.of(item));
        when(bookingRepository.findById(booking.getItemId())).thenReturn(Optional.of(booking));
        when(dbUserStorage.getUserById(userId)).thenReturn(user);
        when(itemRepository.save(item)).thenReturn(item);
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        var actualResponse = dbBookingStorage.approved(userId, bookingId, true);

        assertEquals(bookingDto, actualResponse);
    }

    @Test
    void approvedTestCurrentBookingBeforeNextBook() throws ValidationException {
        var userId = 1L;
        var itemId = 1L;
        var bookingId = 1l;
        var user = getUser(1L, "user", "user@user.com");
        var userDto = getUserDto(2L, "user", "user@user.com");
        var item = getItem(1L, "Дрель", "Простая дрель", true, 1L);
        item.setLastBookingId(1L);
        item.setNextBookingId(1L);
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);
        var start = LocalDateTime.now().plusMinutes(10);
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, itemId, 2L, Status.WAITING, 4L);
        var bookingDto = getBookingDto(1L, start, end, itemDto, userDto, Status.WAITING, 4L);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(itemRepository.findById(booking.getItemId())).thenReturn(Optional.of(item));
//        when(bookingRepository.findById(booking.getItemId())).thenReturn(Optional.of(booking));
        when(dbUserStorage.getUserById(userId)).thenReturn(null);
        when(itemRepository.save(item)).thenReturn(item);
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        var actualResponse = dbBookingStorage.approved(userId, bookingId, true);

        assertEquals(bookingDto, actualResponse);
    }

    @Test
    void approvedTestCurrentBookingBeforeNextBook1() throws ValidationException {
        var userId = 1L;
        var itemId = 1L;
        var bookingId = 1L;
        var user = getUser(1L, "user", "user@user.com");
        var userDto = getUserDto(2L, "user", "user@user.com");
        var item = getItem(1L, "Дрель", "Простая дрель", true, 1L);
        item.setLastBookingId(1L);
        item.setNextBookingId(2L);
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);
        var startCurrentBook = LocalDateTime.now().plusMinutes(10);
        var startNextBook = LocalDateTime.now().plusMinutes(40);
        var endCurrentBook = LocalDateTime.now().plusMinutes(10);
        var endNextBook = LocalDateTime.now().plusMinutes(40);
        var curBooking = getBooking(1L, startCurrentBook, endCurrentBook, itemId, 2L, Status.WAITING, 4L);
        var nextBooking = getBooking(1L, startNextBook, endNextBook, itemId, 2L, Status.WAITING, 4L);

        var bookingDto = getBookingDto(1L, startCurrentBook, endCurrentBook, itemDto, userDto, Status.WAITING, 4L);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(curBooking));
        when(itemRepository.findById(curBooking.getItemId())).thenReturn(Optional.of(item));
        when(bookingRepository.findById(item.getNextBookingId())).thenReturn(Optional.of(nextBooking));
        when(dbUserStorage.getUserById(userId)).thenReturn(null);
        when(itemRepository.save(item)).thenReturn(item);
        when(bookingRepository.save(curBooking)).thenReturn(curBooking);
        when(bookingMapper.toBookingDto(curBooking)).thenReturn(bookingDto);

        var actualResponse = dbBookingStorage.approved(userId, bookingId, true);

        assertEquals(bookingDto, actualResponse);
    }

    @Test
    void approvedTestCurrentBookingBeforeNextBookBeforeNow() throws ValidationException {
        var userId = 1L;
        var itemId = 1L;
        var bookingId = 1l;
        var user = getUser(1L, "user", "user@user.com");
        var userDto = getUserDto(2L, "user", "user@user.com");
        var item = getItem(1L, "Дрель", "Простая дрель", true, 1L);
        item.setLastBookingId(1L);
        item.setNextBookingId(1L);
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);
        var start = LocalDateTime.now().minusMinutes(10);
        var end = LocalDateTime.now().minusMinutes(8);
        var booking = getBooking(1L, start, end, itemId, 2L, Status.WAITING, 4L);
        var bookingDto = getBookingDto(1L, start, end, itemDto, userDto, Status.WAITING, 4L);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(itemRepository.findById(booking.getItemId())).thenReturn(Optional.of(item));
//        when(bookingRepository.findById(booking.getItemId())).thenReturn(Optional.of(booking));
        when(dbUserStorage.getUserById(userId)).thenReturn(null);
        when(itemRepository.save(item)).thenReturn(item);
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        var actualResponse = dbBookingStorage.approved(userId, bookingId, true);

        assertEquals(bookingDto, actualResponse);
    }

    @Test
    void approvedTestCurrentBookingAprrovetFalse() throws ValidationException {
        var userId = 1L;
        var itemId = 1L;
        var bookingId = 1l;
        var user = getUser(1L, "user", "user@user.com");
        var userDto = getUserDto(2L, "user", "user@user.com");
        var item = getItem(1L, "Дрель", "Простая дрель", true, 1L);
        item.setLastBookingId(1L);
        item.setNextBookingId(1L);
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);
        var start = LocalDateTime.now().minusMinutes(10);
        var end = LocalDateTime.now().minusMinutes(8);
        var booking = getBooking(1L, start, end, itemId, 2L, Status.WAITING, 4L);
        var bookingDto = getBookingDto(1L, start, end, itemDto, userDto, Status.WAITING, 4L);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(itemRepository.findById(booking.getItemId())).thenReturn(Optional.of(item));
//        when(bookingRepository.findById(booking.getItemId())).thenReturn(Optional.of(booking));
        when(dbUserStorage.getUserById(userId)).thenReturn(null);
//        when(itemRepository.save(item)).thenReturn(item);
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        var actualResponse = dbBookingStorage.approved(userId, bookingId, false);

        assertEquals(bookingDto, actualResponse);
    }

    @Test
    void approvedTestCurrentBookingAprrovetNull() throws ValidationException {
        var userId = 1L;
        var itemId = 1L;
        var bookingId = 1L;
        var user = getUser(1L, "user", "user@user.com");
        var userDto = getUserDto(2L, "user", "user@user.com");
        var item = getItem(1L, "Дрель", "Простая дрель", true, 1L);
        item.setLastBookingId(1L);
        item.setNextBookingId(1L);
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);
        var start = LocalDateTime.now().minusMinutes(10);
        var end = LocalDateTime.now().minusMinutes(8);
        var booking = getBooking(1L, start, end, itemId, 2L, Status.WAITING, 4L);
        var bookingDto = getBookingDto(1L, start, end, itemDto, userDto, Status.WAITING, 4L);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(itemRepository.findById(booking.getItemId())).thenReturn(Optional.of(item));
//        when(bookingRepository.findById(booking.getItemId())).thenReturn(Optional.of(booking));
        when(dbUserStorage.getUserById(userId)).thenReturn(null);
//        when(itemRepository.save(item)).thenReturn(item);
//        when(bookingRepository.save(booking)).thenReturn(booking);
//        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        assertThrows(ValidationException.class, () -> dbBookingStorage.approved(userId, bookingId, null));
    }

    @Test
    void approvedItemId99Test() {
        var userId = 1L;
        var itemId = 99L;
        var bookingId = 1L;
        var start = LocalDateTime.now().plusMinutes(10);
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, itemId, 2L, Status.WAITING, 4L);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(itemRepository.findById(booking.getItemId())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class,
                ()-> dbBookingStorage.approved(userId, bookingId, true),
                "Вещь не найден");
    }

    @Test
    void approvedBookingId99Test() {
        var userId = 1L;
        var itemId = 1L;
        var bookingId = 99L;
        var start = LocalDateTime.now().plusMinutes(10);
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, itemId, 2L, Status.WAITING, 4L);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());


        assertThrows(ObjectNotFoundException.class,
                ()-> dbBookingStorage.approved(userId, bookingId, true),
                "Запрос на бронирование не найден");
    }
    @Test
    void approvedApprovedTest() {
        var userId = 1L;
        var itemId = 1L;
        var bookingId = 1L;
        var item = getItem(1L, "Дрель", "Простая дрель", true, 1L);
        var start = LocalDateTime.now().plusMinutes(10);
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, itemId, 2L, Status.APPROVED, 4L);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(itemRepository.findById(booking.getItemId())).thenReturn(Optional.of(item));
        when(bookingRepository.findById(booking.getItemId())).thenReturn(Optional.of(booking));

        assertThrows(ValidationException.class,
                ()-> dbBookingStorage.approved(userId, bookingId, true),
                "Бронирование уже было подтверждено ранее");
    }

    @Test
    void approvedOwnerTest() {
        var userId = 1L;
        var itemId = 1L;
        var bookingId = 1L;
        var item = getItem(1L, "Дрель", "Простая дрель", true, 4L);
        var start = LocalDateTime.now().plusMinutes(10);
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, itemId, 2L, Status.WAITING, 4L);
        var bookingForGetItemDto = getBookingForGetItemDto(1L, 2L);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(itemRepository.findById(booking.getItemId())).thenReturn(Optional.of(item));
        when(bookingRepository.findById(booking.getItemId())).thenReturn(Optional.of(booking));

        assertThrows(ObjectNotFoundException.class,
                ()-> dbBookingStorage.approved(userId, bookingId, true),
                "Подтверждение бронирования может делать только владелец вещи");
    }

    @Test
    void approvedWaitingTest() {
        var userId = 1L;
        var itemId = 1L;
        var bookingId = 1L;
        var item = getItem(1L, "Дрель", "Простая дрель", true, 1L);;
        var start = LocalDateTime.now().plusMinutes(10);
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, itemId, 2L, Status.PAST, 4L);
        var bookingForGetItemDto = getBookingForGetItemDto(1L, 2L);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(itemRepository.findById(booking.getItemId())).thenReturn(Optional.of(item));
        when(bookingRepository.findById(booking.getItemId())).thenReturn(Optional.of(booking));

        assertThrows(ObjectNotFoundException.class,
                ()-> dbBookingStorage.approved(userId, bookingId, true),
                "Ответ по бронированию уже дан");
    }

    @Test
    void getBookingDtoByIdTest() {
        var userId = 1L;
        var itemId = 1L;
        var bookingId = 1L;
        var item = getItem(1L, "Дрель", "Простая дрель", true, 4L);
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 4L);
        var user = getUser(1L, "user", "user@user.com");
        var userDto = getUserDto(1L, "user", "user@user.com");
        var start = LocalDateTime.now().plusMinutes(10);
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, itemId, userId, Status.WAITING, 4L);
        var bookingDto = getBookingDto(1L, start, end, itemDto, userDto, Status.APPROVED, 4L);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(itemRepository.findById(booking.getItemId())).thenReturn(Optional.of(item));
        when(dbUserStorage.getUserById(userId)).thenReturn(user);
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        var actualResponse = dbBookingStorage.getBookingDtoById(userId, bookingId);

        assertEquals(bookingDto, actualResponse);
    }

    @Test
    void getBookingDtoBookerIdIdTest() {
        var userId = 1L;
        var itemId = 1L;
        var bookingId = 1L;
        var item = getItem(1L, "Дрель", "Простая дрель", true, 1L);
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);
        var user = getUser(1L, "user", "user@user.com");
        var userDto = getUserDto(1L, "user", "user@user.com");
        var start = LocalDateTime.now().plusMinutes(10);
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, itemId, userId, Status.WAITING, 4L);
        var bookingDto = getBookingDto(1L, start, end, itemDto, userDto, Status.APPROVED, 4L);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(itemRepository.findById(booking.getItemId())).thenReturn(Optional.of(item));
        when(dbUserStorage.getUserById(userId)).thenReturn(user);
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        var actualResponse = dbBookingStorage.getBookingDtoById(userId, bookingId);

        assertEquals(bookingDto, actualResponse);
    }

    @Test
    void getBookingDtoUserId99Test() {
        var userId = 99L;
        var itemId = 1L;
        var bookingId = 1L;
        var item = getItem(1L, "Дрель", "Простая дрель", true, 4L);
        var user = getUser(1L, "user", "user@user.com");
        var start = LocalDateTime.now().plusMinutes(10);
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, itemId, 1L, Status.WAITING, 4L);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(itemRepository.findById(booking.getItemId())).thenReturn(Optional.of(item));
        when(dbUserStorage.getUserById(userId)).thenReturn(user);

        assertThrows(ObjectNotFoundException.class,
                ()->dbBookingStorage.getBookingDtoById(userId,bookingId),
                String.format("Пользователя создавшего бронирование или владельца вещи под данным %d не существует",
                        userId));
    }

    @Test
    void getBookingDtoBookingId99Test() {
        var userId = 1L;
        var bookingId = 99L;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class,
                ()-> dbBookingStorage.getBookingDtoById(userId, bookingId),
                "Запрос на бронирование не найден");
    }

    @Test
    void getBookingDtoItemId99Test() {
        var userId = 1L;
        var itemId = 99L;
        var bookingId = 1L;
        var start = LocalDateTime.now().plusMinutes(10);
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, itemId, 2L, Status.WAITING, 4L);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(itemRepository.findById(booking.getItemId())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class,
                ()-> dbBookingStorage.getBookingDtoById(userId, bookingId),
                "Вещь не найден");
    }

    @Test
    void getAllBookingsByUser() throws ValidationException {
        var userId = 1L;
        var state = "ALL";
        var from = 1;
        var size = 1;
        var start = LocalDateTime.now().plusMinutes(10);
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, 1L, 2L, Status.WAITING, 4L);
        var bookings = List.of(booking);
        var userDto = getUserDto(2L, "user", "user@user.com");
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);
        var bookingDto = getBookingDto(1L, start, end, itemDto, userDto, Status.APPROVED, 1L);
        var expectedResponse = List.of(bookingDto);

        when(bookingRepository.findByBookerIdOrderByStartDesc(userId)).thenReturn(bookings);
        when(pagination.makePagination(from, size, bookings)).thenReturn(bookings);
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        var actualResponse = dbBookingStorage.getAllBookingsByUser(userId, state, from, size);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getAllBookingsByUserFromAndSizeIsNull() throws ValidationException {
        var userId = 1L;
        var state = "ALL";
        Integer from = null;
        Integer size = null;
        var start = LocalDateTime.now().plusMinutes(10);
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, 1L, 2L, Status.WAITING, 4L);
        var bookings = List.of(booking);
        var userDto = getUserDto(2L, "user", "user@user.com");
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);
        var bookingDto = getBookingDto(1L, start, end, itemDto, userDto, Status.APPROVED, 1L);
        var expectedResponse = List.of(bookingDto);

        when(bookingRepository.findByBookerIdOrderByStartDesc(userId)).thenReturn(bookings);
        when(pagination.makePagination(from, size, bookings)).thenReturn(bookings);
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        var actualResponse = dbBookingStorage.getAllBookingsByUser(userId, state, from, size);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getAllBookingsByUserFromIsMinus() throws ValidationException {
        var userId = 1L;
        var state = "ALL";
        Integer from = -1;
        Integer size = 1;
        var start = LocalDateTime.now().plusMinutes(10);
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, 1L, 2L, Status.WAITING, 4L);
        var bookings = List.of(booking);
        var userDto = getUserDto(2L, "user", "user@user.com");
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);
        var bookingDto = getBookingDto(1L, start, end, itemDto, userDto, Status.APPROVED, 1L);
        var expectedResponse = List.of(bookingDto);

        when(bookingRepository.findByBookerIdOrderByStartDesc(userId)).thenReturn(bookings);

        assertThrows(ValidationException.class, () -> dbBookingStorage.getAllBookingsByUser(userId, state, from, size));

    }

    @Test
    void getAllBookingsByUserSattusWainting() throws ValidationException {
        var userId = 1L;
        var state = "WAITING";
        Integer from = 1;
        Integer size = 1;
        var start = LocalDateTime.now().plusMinutes(10);
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, 1L, 2L, Status.WAITING, 4L);
        var bookings = List.of(booking);
        var userDto = getUserDto(2L, "user", "user@user.com");
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);
        var bookingDto = getBookingDto(1L, start, end, itemDto, userDto, Status.WAITING, 1L);
        var expectedResponse = List.of(bookingDto);

        when(bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, Status.valueOf(state))).thenReturn(bookings);
        when(pagination.makePagination(from, size, bookings)).thenReturn(bookings);
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        var actualResponse = dbBookingStorage.getAllBookingsByUser(userId, state, from, size);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getAllBookingsByUserStatusRejected() throws ValidationException {
        var userId = 1L;
        var state = "REJECTED";
        Integer from = 1;
        Integer size = 1;
        var start = LocalDateTime.now().plusMinutes(10);
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, 1L, 2L, Status.WAITING, 4L);
        var bookings = List.of(booking);
        var userDto = getUserDto(2L, "user", "user@user.com");
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);
        var bookingDto = getBookingDto(1L, start, end, itemDto, userDto, Status.WAITING, 1L);
        var expectedResponse = List.of(bookingDto);

        when(bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, Status.valueOf(state))).thenReturn(bookings);
        when(pagination.makePagination(from, size, bookings)).thenReturn(bookings);
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        var actualResponse = dbBookingStorage.getAllBookingsByUser(userId, state, from, size);

        assertEquals(expectedResponse, actualResponse);
    }

//    @Test
//    void getAllBookingsByUserStatusCurrent() throws ValidationException {
//        var userId = 1L;
//        var state = "CURRENT";
//        Integer from = 1;
//        Integer size = 1;
//        var start = LocalDateTime.now().plusMinutes(10);
//        var end = LocalDateTime.now().plusHours(10);
//        var booking = getBooking(1L, start, end, 1L, 2L, Status.WAITING, 4L);
//        var bookings = List.of(booking);
//        var userDto = getUserDto(2L, "user", "user@user.com");
//        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);
//        var bookingDto = getBookingDto(1L, start, end, itemDto, userDto, Status.WAITING, 1L);
//        var expectedResponse = List.of(bookingDto);
//        var now = LocalDateTime.now();
//
//        when(bookingRepository.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, eq(now), eq(now))).thenReturn(bookings);
//        when(pagination.makePagination(from, size, bookings)).thenReturn(bookings);
//        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);
//
//        var actualResponse = dbBookingStorage.getAllBookingsByUser(userId, state, from, size);
//
//        assertEquals(expectedResponse, actualResponse);
//    }

    @Test
    void getAllBookingsByUserStatusFUTURE() throws ValidationException {
        var userId = 1L;
        var state = "FUTURE";
        Integer from = 1;
        Integer size = 1;
        var start = LocalDateTime.now().plusMinutes(10);
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, 1L, 2L, Status.WAITING, 4L);
        var bookings = List.of(booking);
        var userDto = getUserDto(2L, "user", "user@user.com");
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);
        var bookingDto = getBookingDto(1L, start, end, itemDto, userDto, Status.WAITING, 1L);
        var expectedResponse = List.of(bookingDto);
        var now = LocalDateTime.now();

        when(bookingRepository.findByBookerIdOrderByStartDesc(userId)).thenReturn(bookings);
        when(pagination.makePagination(from, size, bookings)).thenReturn(bookings);
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        var actualResponse = dbBookingStorage.getAllBookingsByUser(userId, state, from, size);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getAllBookingsByUserStatusWRONG() throws ValidationException {
        var userId = 1L;
        var state = "ssssss";
        Integer from = 1;
        Integer size = 1;
        var start = LocalDateTime.now().plusMinutes(10);
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, 1L, 2L, Status.WAITING, 4L);
        var bookings = List.of(booking);
        var userDto = getUserDto(2L, "user", "user@user.com");
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);
        var bookingDto = getBookingDto(1L, start, end, itemDto, userDto, Status.WAITING, 1L);
        var expectedResponse = List.of(bookingDto);
        var now = LocalDateTime.now();

        assertThrows(ValidationException.class, () ->  dbBookingStorage.getAllBookingsByUser(userId, state, from, size));
    }

    @Test
    void getAllBookingsByItemsAll() throws ValidationException {
        var userId = 1L;
        var state = "ALL";
        var from = 1;
        var size = 1;
        var start = LocalDateTime.now().plusMinutes(10);
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, 1L, 2L, Status.APPROVED, 4L);
        var bookings = List.of(booking);
        var testBook = new Booking();
        List<Booking> testBooks = List.of(testBook);
        var userDto = getUserDto(2L, "user", "user@user.com");
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);
        var bookingDto = getBookingDto(1L, start, end, itemDto, userDto, Status.APPROVED, 1L);
        var item = getItem(1L, "Дрель", "Простая дрель", true, 4L);
        var items = List.of(item);
        var expectedResponse = List.of(bookingDto);

        when(dbUserStorage.getUserById(userId)).thenReturn(null);
        when(dbItemStorage.getAllItems(userId)).thenReturn(items);
        when(bookingRepository.findByItemIdOrderByStartDesc(item.getId())).thenReturn(testBooks);
//        when(pagination.makePagination(from, size, eq(bookings))).thenReturn(bookings);
        when(pagination.makePagination(from, size, testBooks)).thenReturn(bookings);
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        var actualResponse = dbBookingStorage.getAllBookingsByItems(userId, state, from, size);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getAllBookingsByItemsWAITING() throws ValidationException {
        var userId = 1L;
        var state = "WAITING";
        var from = 1;
        var size = 1;
        var start = LocalDateTime.now().plusMinutes(10);
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, 1L, 2L, Status.APPROVED, 4L);
        var bookings = List.of(booking);
        var testBook = new Booking();
        List<Booking> testBooks = List.of(testBook);
        var userDto = getUserDto(2L, "user", "user@user.com");
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);
        var bookingDto = getBookingDto(1L, start, end, itemDto, userDto, Status.APPROVED, 1L);
        var item = getItem(1L, "Дрель", "Простая дрель", true, 4L);
        var items = List.of(item);
        var expectedResponse = List.of(bookingDto);

        when(dbUserStorage.getUserById(userId)).thenReturn(null);
        when(dbItemStorage.getAllItems(userId)).thenReturn(items);
        when(bookingRepository.findByItemIdAndStatusOrderByStartDesc(
                item.getId(),
                Status.valueOf(state))).thenReturn(testBooks);
//        when(pagination.makePagination(from, size, eq(bookings))).thenReturn(bookings);
        when(pagination.makePagination(from, size, testBooks)).thenReturn(bookings);
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        var actualResponse = dbBookingStorage.getAllBookingsByItems(userId, state, from, size);

        assertEquals(expectedResponse, actualResponse);
    }

//    @Test
//    void getAllBookingsByItemsCURRENT() throws ValidationException {
//        var userId = 1L;
//        var state = "CURRENT";
//        var from = 1;
//        var size = 1;
//        var start = LocalDateTime.now().plusMinutes(10);
//        var end = LocalDateTime.now().plusHours(10);
//        var booking = getBooking(1L, start, end, 1L, 2L, Status.APPROVED, 4L);
//        var bookings = List.of(booking);
//        var testBook = new Booking();
//        List<Booking> testBooks = List.of(testBook);
//        var userDto = getUserDto(2L, "user", "user@user.com");
//        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);
//        var bookingDto = getBookingDto(1L, start, end, itemDto, userDto, Status.APPROVED, 1L);
//        var item = getItem(1L, "Дрель", "Простая дрель", true, 4L);
//        item.setLastBookingId(2L);
//        var items = List.of(item);
//        var expectedResponse = List.of(bookingDto);
//
//        when(dbUserStorage.getUserById(userId)).thenReturn(null);
//        when(dbItemStorage.getAllItems(userId)).thenReturn(items);
//        when(bookingRepository.findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
//                userId, any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(testBooks);
////        when(pagination.makePagination(from, size, eq(bookings))).thenReturn(bookings);
//        when(pagination.makePagination(from, size, testBooks)).thenReturn(bookings);
//        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);
//
//        var actualResponse = dbBookingStorage.getAllBookingsByItems(userId, state, from, size);
//
//        assertEquals(expectedResponse, actualResponse);
//    }

    @Test
    void getAllBookingsByItemsFUTURE() throws ValidationException {
        var userId = 1L;
        var state = "FUTURE";
        var from = 1;
        var size = 1;
        var start = LocalDateTime.now().plusMinutes(10);
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, 1L, 2L, Status.WAITING, 4L);
        var bookings = List.of(booking);
        var testBook = new Booking();
        List<Booking> testBooks = List.of(testBook);
        var userDto = getUserDto(2L, "user", "user@user.com");
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);
        var bookingDto = getBookingDto(1L, start, end, itemDto, userDto, Status.WAITING, 1L);
        var item = getItem(1L, "Дрель", "Простая дрель", true, 4L);
        var items = List.of(item);
        var expectedResponse = List.of(bookingDto);

        when(dbUserStorage.getUserById(userId)).thenReturn(null);
        when(dbItemStorage.getAllItems(userId)).thenReturn(items);
        when(bookingRepository.findByItemIdOrderByStartDesc(item.getId())).thenReturn(bookings);
//        when(pagination.makePagination(from, size, eq(bookings))).thenReturn(bookings);
        when(pagination.makePagination(from, size, bookings)).thenReturn(bookings);
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        var actualResponse = dbBookingStorage.getAllBookingsByItems(userId, state, from, size);

        assertEquals(expectedResponse, actualResponse);
    }

//    @Test
//    void getAllBookingsByItemsPAST() throws ValidationException {
//        var userId = 1L;
//        var state = "PAST";
//        var from = 1;
//        var size = 1;
//        var start = LocalDateTime.now().plusMinutes(10);
//        var end = LocalDateTime.now().plusHours(10);
//        var booking = getBooking(1L, start, end, 1L, 2L, Status.WAITING, 4L);
//        var bookings = List.of(booking);
//        var testBook = new Booking();
//        List<Booking> testBooks = List.of(testBook);
//        var userDto = getUserDto(2L, "user", "user@user.com");
//        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);
//        var bookingDto = getBookingDto(1L, start, end, itemDto, userDto, Status.WAITING, 1L);
//        var item = getItem(1L, "Дрель", "Простая дрель", true, 4L);
//        var items = List.of(item);
//        var expectedResponse = List.of(bookingDto);
//
//        when(dbUserStorage.getUserById(userId)).thenReturn(null);
//        when(dbItemStorage.getAllItems(userId)).thenReturn(items);
//        when(bookingRepository.findByItemIdOrderByStartDesc(item.getId())).thenReturn(bookings);
////        when(pagination.makePagination(from, size, eq(bookings))).thenReturn(bookings);
//        when(pagination.makePagination(from, size, bookings)).thenReturn(bookings);
//        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);
//
//        var actualResponse = dbBookingStorage.getAllBookingsByItems(userId, state, from, size);
//
//        assertEquals(expectedResponse, actualResponse);
//    }

    @Test
    void getAllBookingsByItemsWRONG() throws ValidationException {
        var userId = 1L;
        var state = "ssss";
        var from = 1;
        var size = 1;
        var start = LocalDateTime.now().plusMinutes(10);
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, 1L, 2L, Status.WAITING, 4L);
        var bookings = List.of(booking);
        var testBook = new Booking();
        List<Booking> testBooks = List.of(testBook);
        var userDto = getUserDto(2L, "user", "user@user.com");
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);
        var bookingDto = getBookingDto(1L, start, end, itemDto, userDto, Status.WAITING, 1L);
        var item = getItem(1L, "Дрель", "Простая дрель", true, 4L);
        var items = List.of(item);
        var expectedResponse = List.of(bookingDto);

        when(dbUserStorage.getUserById(userId)).thenReturn(null);
        when(dbItemStorage.getAllItems(userId)).thenReturn(items);
//        when(bookingRepository.findByItemIdOrderByStartDesc(item.getId())).thenReturn(bookings);
//        when(pagination.makePagination(from, size, eq(bookings))).thenReturn(bookings);
//        when(pagination.makePagination(from, size, bookings)).thenReturn(bookings);
//        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        assertThrows(ValidationException.class, () ->  dbBookingStorage.getAllBookingsByItems(userId, state, from, size));
    }

    @Test
    void getAllBookingsByItemspaginationMinus() throws ValidationException {
        var userId = 1L;
        var state = "ALL";
        var from = -1;
        var size = 1;
        var start = LocalDateTime.now().plusMinutes(10);
        var end = LocalDateTime.now().plusHours(10);
        var booking = getBooking(1L, start, end, 1L, 2L, Status.WAITING, 4L);
        var bookings = List.of(booking);
        var testBook = new Booking();
        List<Booking> testBooks = List.of(testBook);
        var userDto = getUserDto(2L, "user", "user@user.com");
        var itemDto = getItemDto(1L, "Дрель", "Простая дрель", true, 1L);
        var bookingDto = getBookingDto(1L, start, end, itemDto, userDto, Status.WAITING, 1L);
        var item = getItem(1L, "Дрель", "Простая дрель", true, 4L);
        var items = List.of(item);
        var expectedResponse = List.of(bookingDto);

        when(dbUserStorage.getUserById(userId)).thenReturn(null);
        when(dbItemStorage.getAllItems(userId)).thenReturn(items);
        when(bookingRepository.findByItemIdOrderByStartDesc(item.getId())).thenReturn(bookings);
//        when(pagination.makePagination(from, size, eq(bookings))).thenReturn(bookings);
//        when(pagination.makePagination(from, size, bookings)).thenReturn(bookings);
//        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        assertThrows(ValidationException.class, () ->  dbBookingStorage.getAllBookingsByItems(userId, state, from, size));
    }


}