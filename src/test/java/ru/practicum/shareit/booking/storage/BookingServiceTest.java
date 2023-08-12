package ru.practicum.shareit.booking.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.services.BookingServices;
import ru.practicum.shareit.exceptions.ValidationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingStorage bookingStorage;
    @InjectMocks
    private BookingServices bookingServices;


    @Test
    void create() throws ValidationException {
        var booking = new Booking();
        var bookingDto = BookingDto.builder().build();
        when(bookingStorage.create(1L, booking)).thenReturn(bookingDto);

        var actualResponse = bookingServices.create(1L, booking);

        verify(bookingStorage).create(1L, booking);
        assertEquals(bookingDto, actualResponse);
    }
}
