package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.services.BookingServices;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {

    private final BookingServices bookingServices;

    @PostMapping
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                             @RequestBody Booking booking) throws ValidationException {
        log.info("Получен запрос на создание бронирования пользователем{}", userId);
        return bookingServices.create(userId, booking);
    }

    @PatchMapping("{bookingId}")
    public BookingDto approved(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @PathVariable("bookingId") Long bookingId,
                               @RequestParam Boolean approved) throws ValidationException {
        log.info("Получен запрос на подтверждение бронирование пользователем");
        return bookingServices.approved(userId, bookingId, approved);
    }

    @GetMapping("{bookingId}")
    public BookingDto getBookingId(@PathVariable("bookingId") Long bookingId){
        log.info("Получен запрос на получение бронирования {}", bookingId);
        return bookingServices.getBookingId(bookingId);
    }

    @GetMapping
    public List<BookingDto> getAllBookingsByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @RequestParam (required = false) String state) {
        log.info("Получен запрос на список бронирования текущего пользователя {}", userId);
        return bookingServices.getAllBookingsByUser(userId, state);
    }

    @GetMapping("owner")
    public List<BookingDto> getAllBookingsByItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable("itemId") Long itemId,
                                                 @RequestParam (required = false) String state) {
        log.info("Получен запрос на список бронирования текущего пользователя {}", userId);
        return bookingServices.getAllBookingsByItems(userId, itemId, state);
    }
}
