package ru.practicum.shareit.booking.storage;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.ValidationException;

import java.util.List;

public interface BookingStorage {

    BookingDto create(Long userId, Booking booking) throws ValidationException;

    BookingDto approved(Long userId, Long bookingId, Boolean approved) throws ValidationException;

    BookingDto getBookingDtoById(Long userId, Long bookingId) throws ValidationException;

    List<BookingDto> getAllBookingsByUser(Long userId, String state) throws ValidationException;

    List<BookingDto> getAllBookingsByItems(Long userId, String state) throws ValidationException;
}
