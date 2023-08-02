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
import ru.practicum.shareit.item.storage.DBItemStorageImpl;
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

    @Override
    public BookingDto create(Long userId, Booking booking) throws ValidationException {
        User user = dbUserStorage.getUserById(userId);
        Item item = dbItemStorage.getItemById(booking.getItemId());
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь недоступна для бронирования");
        }
        if (booking.getStart() == null ||
                booking.getEnd() == null ||
                booking.getStart().isAfter(booking.getEnd()) ||
                booking.getStart().equals(booking.getEnd()) ||
                booking.getEnd().isBefore(LocalDateTime.now()) ||
                booking.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Неверно указана дата бронирования");
        } else if (item.getOwnerId().equals(userId)){
            throw new ObjectNotFoundException("Владелец вещи пытается сделать бронирование своей вещи");
        }
        if (booking.getStatus() == null) {
            booking.setStatus(Status.WAITING);
        }
        booking.setBookerId(user.getId());
        Booking bookingBase = bookingRepository.save(booking);
        return bookingMapper.toBookingDto(bookingBase);
    }

    @Override
    public BookingDto approved(Long userId, Long bookingId, Boolean approved) throws ValidationException {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new ObjectNotFoundException("Запрос на бронирование не найден"));
        Item item = dbItemStorage.getItemById(booking.getItemId());
        dbUserStorage.getUserById(userId);

        if(item.getOwnerId().equals(userId) && booking.getStatus().equals(Status.APPROVED)){
            throw new ValidationException("Бронирование уже было подтверждено ранее");
        }

        if (item.getAvailable() && item.getOwnerId().equals(userId) && approved != null) {
            if (approved) {
                booking.setStatus(Status.APPROVED);
            } else {
                booking.setStatus(Status.REJECTED);
            }
        } else if(!item.getOwnerId().equals(userId)) {
            throw new ObjectNotFoundException("Подтверждение бронирования может делать только владелец вещи");
        }
        else {
            throw new ValidationException("Неверные данные");
        }

        booking = bookingRepository.save(booking);
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getBookingDtoById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new ObjectNotFoundException("Запрос на бронирование не найден"));
        Item item = dbItemStorage.getItemById(booking.getItemId());
        dbUserStorage.getUserById(userId);

        if (item.getOwnerId().equals(userId)) {
            return bookingMapper.toBookingDto(booking);
        }  else if (booking.getBookerId().equals(userId)) {
            return bookingMapper.toBookingDto(booking);
        } else {
            throw new ObjectNotFoundException(String.format
                    ("Пользователя создавшего бронирование или владельца вещи под данным %d не существует", userId));
        }
    }

    @Override
    public List<BookingDto> getAllBookingsByUser(Long userId, String state) throws ValidationException {
        dbUserStorage.getUserById(userId);
        List<Booking> bookingList = new ArrayList<>();

        if (state == null || state.equals("ALL")) {
            bookingList = bookingRepository.findByBookerIdOrderByStartDesc(userId);
        } else if (state.equals(Status.CURRENT.name()) ||
                state.equals(Status.WAITING.name()) ||
                state.equals(Status.REJECTED.name())) {
            bookingList = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, state);
        } else if (state.equals(Status.FUTURE.name())) {
            List<Booking> bookings = bookingRepository.findByBookerIdOrderByStartDesc(userId);
            for (Booking booking : bookings) {
                if (booking.getStatus().equals(Status.WAITING) || booking.getStatus().equals(Status.APPROVED)) {
                    bookingList.add(booking);
                    var i = 0;
                }
            }
        } else if (state.equals(Status.PAST.name())) {
            List<Booking> bookings = bookingRepository.findByBookerIdOrderByStartDesc(userId);
            for (Booking booking : bookings) {
                if (booking.getStatus().equals(Status.REJECTED) ||
                        booking.getStatus().equals(Status.CANCELED) ||
                        booking.getEnd().isBefore(LocalDateTime.now())) {
                    bookingList.add(booking);
                }
            }
        } else {
            throw new ValidationException(String.format("Unknown state: %s", state));
        }

        return bookingList.stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getAllBookingsByItems(Long userId, String state) throws ValidationException {
        dbUserStorage.getUserById(userId);
        List<Item> items = dbItemStorage.getFindAllItems(userId);
        List<Booking> bookingList = new ArrayList<>();

        for (Item item : items) {
            if (state == null || state.equals("ALL")) {
                List<Booking> booking = bookingRepository.findByItemIdOrderByStartDesc(item.getId());
                bookingList.addAll(booking);
            } else if (state.equals(Status.CURRENT.name()) ||
                    state.equals(Status.WAITING.name()) ||
                    state.equals(Status.REJECTED.name())) {
                List<Booking> booking = bookingRepository.findByItemIdAndStatusOrderByStartDesc(
                        item.getId(),
                        state);
                bookingList.addAll(booking);
            } else if (state.equals(Status.FUTURE.name())) {
                List<Booking> bookings = bookingRepository.findByItemIdOrderByStartDesc(item.getId());
                for (Booking booking : bookings) {
                    if (booking.getStatus().equals(Status.WAITING) || booking.getStatus().equals(Status.APPROVED)) {
                        bookingList.add(booking);
                    }
                }
            } else if (state.equals(Status.PAST.name())) {
                List<Booking> bookings = bookingRepository.findByItemIdOrderByStartDesc(item.getId());
                for (Booking booking : bookings) {
                    if (booking.getStatus().equals(Status.REJECTED) ||
                            booking.getStatus().equals(Status.CANCELED) ||
                            booking.getEnd().isBefore(LocalDateTime.now())) {
                        bookingList.add(booking);
                    }
                }
            }else {
                throw new ValidationException(String.format("Unknown state: %s", state));
            }
        }

        return bookingList.stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
