package ru.practicum.shareit.booking.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exceptions.ValidationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class BookingServicesTest {

    @Mock
    private BookingStorage bookingStorage;
    @InjectMocks
    private BookingServices bookingServices;

    @Test
    void createTest() throws ValidationException {
        var booking = new Booking();
        var bookingDto = BookingDto.builder().build();

        when(bookingStorage.create(1L, booking)).thenReturn(bookingDto);

        var actualResponse = bookingServices.create(1L, booking);

        assertEquals(bookingDto, actualResponse);
    }

    @Test
    void approvedTest() throws ValidationException {
        var approved = true;
        var bookingDto = BookingDto.builder().build();

        when(bookingStorage.approved(1L, 1L, approved)).thenReturn(bookingDto);

        var actualResponse = bookingServices.approved(1L, 1L, approved);

        assertEquals(bookingDto, actualResponse);
    }

    @Test
    void getBookingIdTest() throws ValidationException {
        var bookingDto = BookingDto.builder().build();

        when(bookingStorage.getBookingDtoById(1L, 1L)).thenReturn(bookingDto);

        var actualResponse = bookingServices.getBookingId(1L, 1L);

        assertEquals(bookingDto, actualResponse);
    }

    @Test
    void getAllBookingsByUserTest() throws ValidationException {
        var bookingDto = BookingDto.builder().build();
        var bookings = List.of(bookingDto);

        when(bookingStorage.getAllBookingsByUser(1L, "WAITING", 0, 20)).thenReturn(bookings);

        var actualResponse = bookingServices.getAllBookingsByUser(1L, "WAITING", 0, 20);

        assertEquals(bookings, actualResponse);
    }

    @Test
    void getAllBookingsByItemsTest() throws ValidationException {
        var bookingDto = BookingDto.builder().build();
        var bookings = List.of(bookingDto);

        when(bookingStorage.getAllBookingsByItems(1L, "WAITING", 0, 20)).thenReturn(bookings);

        var actualResponse = bookingServices.getAllBookingsByItems(1L, "WAITING", 0, 20);

        assertEquals(bookings, actualResponse);
    }
}