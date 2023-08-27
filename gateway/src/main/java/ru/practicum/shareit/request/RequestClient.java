package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Service
public class RequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createRequest(Long userId, ItemRequestDto itemRequest) throws ValidationException {
        if (itemRequest.getDescription() == null ||
                itemRequest.getDescription().isEmpty() ||
                itemRequest.getDescription().isBlank()) {
            throw new ValidationException("Описание запроса отсутствует");
        }
        return patch("", userId, itemRequest);
    }

    public ResponseEntity<Object> getAllRequestsByUser(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getAllRequests(Long userId, Integer from, Integer size) throws ValidationException {
        if (from < 0 || size < 0 || size == 0) {
            throw new ValidationException("Индекс первого элемента и размер листа не может быть меньше нуля");
        }
        return get("/all?from={from}&size={size}", userId);
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> getRequests(Long userId, Long requestId) throws ValidationException {
        if (requestId < 0) {
            throw new ValidationException("Значение requestId не может быть меньше нуля");
        }
        return get("/" + requestId, userId);
    }
}
