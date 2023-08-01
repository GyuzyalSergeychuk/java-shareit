package ru.practicum.shareit.booking.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exceptions.ValidationException;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServices {

    private final BookingStorage bookingStorage;

    public BookingDto create(Long userId, Booking booking) throws ValidationException {
        return bookingStorage.create(userId, booking);
    }

    public BookingDto approved(Long userId, Long bookingId, Boolean approved) throws ValidationException {
        return bookingStorage.approved(userId, bookingId, approved);
    };

    public BookingDto getBookingId(Long bookingId){
        return bookingStorage.getBookingId(bookingId);
    }

    public List<BookingDto> getAllBookingsByUser(Long userId, String state){
        return bookingStorage.getAllBookingsByUser(userId, state);
    }

    public List<BookingDto> getAllBookingsByItems(Long userId, Long itemId, String state){
        return bookingStorage.getAllBookingsByItems(userId, itemId, state);
    }
}
