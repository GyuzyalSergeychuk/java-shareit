package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.services.BookingServices;
import ru.practicum.shareit.exceptions.ValidationException;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

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
    public BookingDto getBookingId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                   @PathVariable("bookingId") Long bookingId) throws ValidationException {
        log.info("Получен запрос на получение бронирования {}", bookingId);
        return bookingServices.getBookingId(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getAllBookingsByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @RequestParam(required = false) String state,
                                                 @RequestParam(name = "from", required = false) Integer from,
                                                 @RequestParam(name = "size", required = false) Integer size)
            throws ValidationException {
        log.info("Получен запрос на список бронирования текущего пользователя {}", userId);
        return bookingServices.getAllBookingsByUser(userId, state, from, size);
    }

    @GetMapping("owner")
    public List<BookingDto> getAllBookingsByItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(required = false) String state,
                                                  @RequestParam(name = "from", required = false) Integer from,
                                                  @RequestParam(name = "size", required = false) Integer size)
            throws ValidationException {
        log.info("Получен запрос на список бронирования текущего пользователя {}", userId);
        return bookingServices.getAllBookingsByItems(userId, state, from, size);
    }
}
