package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.exceptions.ValidationException;

@Controller
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestBody BookingRequestDto booking) {
        log.info("Получен запрос на создание бронирования пользователем{}", userId);
        return bookingClient.createBooking(userId, booking);
    }

    @PatchMapping("{bookingId}")
    public ResponseEntity<Object> approved(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @PathVariable("bookingId") Long bookingId,
                                           @RequestParam Boolean approved) {
        log.info("Получен запрос на подтверждение бронирование пользователем");
        return bookingClient.approved(userId, bookingId, approved);
    }

    @GetMapping("{bookingId}")
    public ResponseEntity<Object> getBookingId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @PathVariable("bookingId") Long bookingId) throws ValidationException {
        log.info("Получен запрос на получение бронирования {}", bookingId);
        return bookingClient.getBookingId(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookingsByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                       @RequestParam(required = false) String state,
                                                       @RequestParam(name = "from", required = false) Integer from,
                                                       @RequestParam(name = "size", required = false) Integer size)
            throws ValidationException {
        log.info("Получен запрос на список бронирования текущего пользователя {}", userId);
        return bookingClient.getAllBookingsByUser(userId, state, from, size);
    }

    @GetMapping("owner")
    public ResponseEntity<Object> getAllBookingsByItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestParam(required = false) String state,
                                                        @RequestParam(name = "from", required = false) Integer from,
                                                        @RequestParam(name = "size", required = false) Integer size)
            throws ValidationException {
        log.info("Получен запрос на список бронирования текущего пользователя {}", userId);
        return bookingClient.getAllBookingsByItems(userId, state, from, size);
    }
}
