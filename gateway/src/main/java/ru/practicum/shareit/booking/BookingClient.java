package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> create(Long userId, BookingDto booking) throws ValidationException {
        if (userId <= 0) {
            throw new ObjectNotFoundException("Id не может быть меньше нуля");
        }
        if (booking.getStart() == null ||
                booking.getEnd() == null ||
                booking.getStart().isAfter(booking.getEnd()) ||
                booking.getStart().equals(booking.getEnd()) ||
                booking.getEnd().isBefore(LocalDateTime.now()) ||
                booking.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Неверно указана дата бронирования");
        }
        return patch("", userId, booking);
    }

    @PatchMapping("{bookingId}")
    public ResponseEntity<Object> approved(Long userId, Long bookingId, Boolean approved) {
        if (bookingId <= 0) {
            throw new ObjectNotFoundException("Id не может быть меньше нуля");
        }
        if (userId <= 0) {
            throw new ObjectNotFoundException("Id не может быть меньше нуля");
        }
        return patch("/" + bookingId, userId, approved);
    }

    public ResponseEntity<Object> getBookingId(Long userId, Long bookingId) {
        if (bookingId <= 0) {
            throw new ObjectNotFoundException("Id не может быть меньше нуля");
        }
        if (userId <= 0) {
            throw new ObjectNotFoundException("Id не может быть меньше нуля");
        }
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getAllBookingsByUser(long userId, String state, Integer from, Integer size) throws ValidationException {
        if (from < 0 || size < 0 || size == 0) {
            throw new ValidationException("Индекс первого элемента и размер листа не может быть меньше нуля");
        }
        Map<String, Object> parameters = Map.of(
                "state", state,
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }


    public ResponseEntity<Object> getAllBookingsByItems(long userId, String state, Integer from, Integer size) throws ValidationException {
        if (from < 0 || size < 0 || size == 0) {
            throw new ValidationException("Индекс первого элемента и размер листа не может быть меньше нуля");
        }
        Map<String, Object> parameters = Map.of(
                "state", state,
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }
}

