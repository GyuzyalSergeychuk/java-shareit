package ru.practicum.shareit.request.storage;

import java.util.List;

import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

public interface ItemRequestStorage {

    ItemRequestDto createRequest(Long userId, ItemRequest itemRequest) throws ValidationException;

    List<ItemRequestDto> getAllRequestsByUser(Long userId) throws ValidationException;

    List<ItemRequestDto> getAllRequests(Long userId, Integer from, Integer size) throws ValidationException;

    ItemRequestDto getRequests(Long userId, Long requestId) throws ValidationException;

}
