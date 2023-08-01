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
import java.util.List;

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

        if (item.getAvailable() && item.getOwnerId().equals(userId) && approved != null) {
            if (approved) {
                booking.setStatus(Status.APPROVED);
            } else {
                booking.setStatus(Status.REJECTED);
            }
        } else {
            throw new ValidationException("Неверные данные");
        }

        booking = bookingRepository.save(booking);
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getBookingId(Long bookingId) {
        return null;
    }

    @Override
    public List<BookingDto> getAllBookingsByUser(Long userId, String state) {
        return null;
    }

    @Override
    public List<BookingDto> getAllBookingsByItems(Long userId, Long itemId, String state) {
        return null;
    }
}
