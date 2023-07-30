package ru.practicum.shareit.booking.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.storage.DBItemStorageImpl;
import ru.practicum.shareit.user.storage.DBUserStorageImpl;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DBBookingStorageImpl implements BookingStorage{

    private final BookingMapper bookingMapper;
    private final DBUserStorageImpl dbUserStorage;
    private final DBItemStorageImpl dbItemService;
    private final BookingRepository bookingRepository;

    @Override
    public BookingDto create(Long userId, Booking booking) {
        return null;
    }

    @Override
    public BookingDto approved(Long userId, Long itemId, Booking booking) {
        return null;
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
