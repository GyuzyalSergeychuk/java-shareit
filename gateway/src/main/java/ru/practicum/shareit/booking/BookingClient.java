package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exceptions.ValidationException;

@Service
@Slf4j
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

    public ResponseEntity<Object> createBooking(Long userId, BookingRequestDto booking) {
        return post("", userId, booking);
    }

    public ResponseEntity<Object> approved(Long userId, Long bookingId, Boolean approved) {
        return patch("/" + bookingId + "?approved=" + approved, userId.intValue());
    }

    public ResponseEntity<Object> getBookingId(Long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getAllBookingsByUser(long userId, String state, Integer from, Integer size) {
        String path = "";
        if (state != null) {
            path = "?state=" + state;
        } else if (size != null) {
            path = "?from=" + from + "&size=" + size;
        }
        return get(path, userId);
    }


    public ResponseEntity<Object> getAllBookingsByItems(long userId, String state, Integer from, Integer size) throws ValidationException {
        String path = "/owner";
        if (state != null) {
            path = "/owner?state=" + state;
        } else if (size != null) {
            path = "/owner?&from=" + from + "&size=" + size;
        }
        return get(path, userId);
    }
}

