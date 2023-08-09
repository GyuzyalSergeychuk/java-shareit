package ru.practicum.shareit.request.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestService {

    private final ItemRequestStorage itemRequestStorage;

    public ItemRequestDto createRequest(Long userId, ItemRequest itemRequest) throws ValidationException {
        return itemRequestStorage.createRequest(userId, itemRequest);
    }

    public List<ItemRequestDto> getAllRequestsByUser(Long userId) throws ValidationException {
        return itemRequestStorage.getAllRequestsByUser(userId);
    }

    public List<ItemRequestDto> getAllRequests(Long userId, Integer from, Integer size) throws ValidationException{
        return itemRequestStorage.getAllRequests(userId, from, size);
    }

    public ItemRequestDto getRequests(Long userId, Long requestId) throws ValidationException{
        return itemRequestStorage.getRequests(userId, requestId);
    }
}
