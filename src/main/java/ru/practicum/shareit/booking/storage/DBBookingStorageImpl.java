package ru.practicum.shareit.booking.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.storage.DBItemStorageImpl;
import ru.practicum.shareit.pagination.Pagination;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.DBUserStorageImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DBBookingStorageImpl implements BookingStorage {

    private final BookingMapper bookingMapper;
    private final DBUserStorageImpl dbUserStorage;
    private final DBItemStorageImpl dbItemStorage;
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final Pagination<Booking> pagination;

    @Override
    public BookingDto create(Long userId, Booking bookingReq) throws ValidationException {
        User user = dbUserStorage.getUserById(userId);
        Item item = itemRepository.findById(bookingReq.getItemId()).orElseThrow(() ->
                new ObjectNotFoundException("Вещь не найдена"));
        bookingReq.setItemOwnerId(item.getOwnerId());
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь недоступна для бронирования");
        }
        if (bookingReq.getStart() == null ||
                bookingReq.getEnd() == null ||
                bookingReq.getStart().isAfter(bookingReq.getEnd()) ||
                bookingReq.getStart().equals(bookingReq.getEnd()) ||
                bookingReq.getEnd().isBefore(LocalDateTime.now()) ||
                bookingReq.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Неверно указана дата бронирования");
        }

        if (item.getOwnerId().equals(userId)) {
            throw new ObjectNotFoundException("Владелец вещи пытается сделать бронирование своей вещи");
        }
        //Запрашиваем все бронирования целевой вещи, фильтруем по статусу АПРУВ, и сверяем время с текущим бронированием.
        //В случае пересечения выбрасываем исключение
        List<Booking> allBookingsOfTargetItem = bookingRepository.findByItemIdOrderByStartDesc(item.getId());
        if (allBookingsOfTargetItem.stream()
                .filter((Booking b) -> Status.APPROVED.equals(b.getStatus()))
                .anyMatch((Booking b) -> b.getStart().isBefore(bookingReq.getEnd()) && b.getEnd().isAfter(bookingReq.getStart()))) {
            throw new ObjectNotFoundException("Предмет нельзя забронировать. Он уже используется кем-то!");
        }
        bookingReq.setStatus(Status.WAITING);
        bookingReq.setBookerId(user.getId());
        Booking booking = bookingRepository.save(bookingReq);
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto approved(Long userId, Long bookingId, Boolean approved) throws ValidationException {
        Booking currentBooking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new ObjectNotFoundException("Запрос на бронирование не найден"));
        Item item = itemRepository.findById(currentBooking.getItemId()).orElseThrow(() ->
                new ObjectNotFoundException("Вещь не найден"));
        Booking nextBooking = null;
        if (item.getNextBookingId() != null) {
            nextBooking = bookingRepository.findById(item.getNextBookingId()).get();
        }
        dbUserStorage.getUserById(userId);
        if (item.getOwnerId().equals(userId) && currentBooking.getStatus().equals(Status.APPROVED)) {
            throw new ValidationException("Бронирование уже было подтверждено ранее");
        }
        if (!item.getOwnerId().equals(userId)) {
            throw new ObjectNotFoundException("Подтверждение бронирования может делать только владелец вещи");
        }
        if (!currentBooking.getStatus().equals(Status.WAITING)) {
            throw new ObjectNotFoundException("Ответ по бронированию уже дан");
        }
        currentBooking = setApprove(currentBooking, nextBooking, approved, item);

        currentBooking = bookingRepository.save(currentBooking);
        return bookingMapper.toBookingDto(currentBooking);
    }

    private Booking setApprove(Booking currentBooking, Booking nextBooking, Boolean approved, Item item) throws ValidationException {
        if (approved != null) {
            if (approved) {
                currentBooking.setStatus(Status.APPROVED);
                // если бронирование подтверждено, обновляем информацию о бронированиях в вещи
                if (item.getLastBookingId() == null) {
                    item.setLastBookingId(currentBooking.getId());
                    item.setNextBookingId(currentBooking.getId());
                } else {
                    if (currentBooking.getEnd().isBefore(nextBooking.getStart())) {
                        item.setNextBookingId(currentBooking.getId());
                    } else {
                        if (nextBooking.getEnd().isBefore(LocalDateTime.now())) {
                            item.setLastBookingId(nextBooking.getId());
                            item.setNextBookingId(currentBooking.getId());
                        } else {
                            item.setNextBookingId(currentBooking.getId());
                        }
                    }
                }
                itemRepository.save(item);
            } else {
                currentBooking.setStatus(Status.REJECTED);
            }
        } else {
            throw new ValidationException("Неверные данные");
        }
        return currentBooking;
    }

    @Override
    public BookingDto getBookingDtoById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new ObjectNotFoundException("Запрос на бронирование не найден"));
        Item item = itemRepository.findById(booking.getItemId()).orElseThrow(() ->
                new ObjectNotFoundException("Вещь не найден"));
        dbUserStorage.getUserById(userId);

        if (item.getOwnerId().equals(userId)) {
            return bookingMapper.toBookingDto(booking);
        } else if (booking.getBookerId().equals(userId)) {
            return bookingMapper.toBookingDto(booking);
        } else {
            throw new ObjectNotFoundException(String.format("Пользователя создавшего бронирование или владельца вещи под данным %d не существует",
                    userId));
        }
    }

    @Override
    public List<BookingDto> getAllBookingsByUser(Long userId, String state, Integer from, Integer size) throws ValidationException {
        dbUserStorage.getUserById(userId);
        List<Booking> bookingList = getAllBooking(userId, state);
        List<Booking> list;
        if (from == null && size == null) {
            list = pagination.makePagination(0, 20, bookingList);
        } else if (from > 0 && size > 0) {
            list = pagination.makePagination(from, size, bookingList);
        } else {
            throw new ValidationException("from and size не могут быть нулями");
        }
        return list.stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private List<Booking> getAllBooking(Long userId, String state) throws ValidationException {
        List<Booking> bookingList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        if (state == null || "ALL".equals(state)) {
            bookingList = bookingRepository.findByBookerIdOrderByStartDesc(userId);
        } else if (state.equals(Status.WAITING.name()) ||
                state.equals(Status.REJECTED.name())) {
            bookingList = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, Status.valueOf(state));
        } else if (state.equals(Status.CURRENT.name())) {
            bookingList = bookingRepository.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                    userId, now, now);
        } else if (state.equals(Status.FUTURE.name())) {
            List<Booking> bookings = bookingRepository.findByBookerIdOrderByStartDesc(userId);
            for (Booking booking : bookings) {
                if (booking.getStatus().equals(Status.WAITING) || booking.getStatus().equals(Status.APPROVED)) {
                    bookingList.add(booking);
                }
            }
        } else if (state.equals(Status.PAST.name())) {
            List<Booking> bookings = bookingRepository.findByBookerIdAndEndBeforeAndStatusEqualsOrderByStartDesc(userId, now, Status.APPROVED);
            bookingList.addAll(bookings);
        } else {
            throw new ValidationException(String.format("Unknown state: %s", state));
        }
        return bookingList;
    }

    @Override
    public List<BookingDto> getAllBookingsByItems(Long userId, String state, Integer from, Integer size) throws ValidationException {
        dbUserStorage.getUserById(userId);
        List<Item> items = dbItemStorage.getAllItems(userId);
        List<Booking> bookingList = getListItems(userId, items, state);
        List<Booking> list;
        if (from == null && size == null) {
            list = pagination.makePagination(0, 20, bookingList);
        } else if (from >= 0 && size > 0) {
            list = pagination.makePagination(from, size, bookingList);
        } else {
            throw new ValidationException("from and size не могут быть нулями");
        }
        return list.stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private List<Booking> getListItems(Long userId, List<Item> items, String state) throws ValidationException {
        List<Booking> bookingList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (Item item : items) {
            if (state == null || "ALL".equals(state)) {
                List<Booking> booking = bookingRepository.findByItemIdOrderByStartDesc(item.getId());
                bookingList.addAll(booking);
            } else if (state.equals(Status.WAITING.name()) ||
                    state.equals(Status.REJECTED.name())) {
                List<Booking> bookings = bookingRepository.findByItemIdAndStatusOrderByStartDesc(
                        item.getId(),
                        Status.valueOf(state));
                bookingList.addAll(bookings);
            } else if (state.equals(Status.CURRENT.name())) {
                if (item.getLastBookingId() != null) {
                    List<Booking> bookings = bookingRepository.findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                            userId, now, now);
                    bookingList.addAll(bookings);
                }
            } else if (state.equals(Status.FUTURE.name())) {
                List<Booking> bookings = bookingRepository.findByItemIdOrderByStartDesc(item.getId());
                for (Booking booking : bookings) {
                    if (booking.getStatus().equals(Status.WAITING) || booking.getStatus().equals(Status.APPROVED)) {
                        bookingList.add(booking);
                    }
                }
            } else if (state.equals(Status.PAST.name())) {
                if (item.getLastBookingId() != null) {
                    List<Booking> bookings = bookingRepository.findByItemOwnerIdAndEndBeforeAndStatusEqualsOrderByStartDesc(
                            userId, now, Status.APPROVED);
                    bookingList.addAll(bookings);
                }
            } else {
                throw new ValidationException(String.format("Unknown state: %s", state));
            }
        }
        return bookingList;
    }
}
