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

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServices {

    private final BookingStorage bookingStorage;

    public BookingDto create(Long userId, Booking booking){
        return bookingStorage.create(userId, booking);
    }

    public BookingDto approved(Long userId, Long itemId, Booking booking){
        return bookingStorage.approved(userId, itemId, booking);
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
