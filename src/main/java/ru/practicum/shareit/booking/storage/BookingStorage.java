package ru.practicum.shareit.booking.storage;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingStorage {

    BookingDto create(Long userId,Booking booking);

    BookingDto approved(Long userId, Long itemId, Booking booking);

    BookingDto getBookingId(Long bookingId);

    List<BookingDto> getAllBookingsByUser(Long userId, String state);

    List<BookingDto> getAllBookingsByItems(Long userId, Long itemId,String state);
}
