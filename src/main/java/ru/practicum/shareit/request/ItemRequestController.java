package ru.practicum.shareit.request;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.services.ItemRequestService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @RequestBody ItemRequest itemRequest) throws ValidationException {
        log.info("Получен запрос на создание request пользователь {}", userId);
        return itemRequestService.createRequest(userId, itemRequest);
    }

    @GetMapping
    public List<ItemRequestDto> getAllRequestsByUser (@RequestHeader("X-Sharer-User-Id") Long userId)
            throws ValidationException {
        log.info("Получен запрос на получения списка всех requests пользователем {}", userId);
        return itemRequestService.getAllRequestsByUser(userId);
    }

    @GetMapping("all")
    public List<ItemRequestDto> getAllRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
            @RequestParam(name = "size", defaultValue = "20") @Min(1) @Max(100) Integer size)
            throws ValidationException {
        return itemRequestService.getAllRequests(userId, from, size);
    }

    @GetMapping("{requestId}")
    public ItemRequestDto getRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable("requestId") Long requestId) throws ValidationException {
        return itemRequestService.getRequests(userId, requestId);
    }
}