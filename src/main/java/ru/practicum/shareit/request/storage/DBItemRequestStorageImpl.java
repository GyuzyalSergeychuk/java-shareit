package ru.practicum.shareit.request.storage;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DBItemRequestStorageImpl implements ItemRequestStorage{

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final UserRepository userRepository;

    @Override
    public ItemRequestDto createRequest(Long userId, ItemRequest itemRequest) throws ValidationException {
        if (itemRequest.getDescription() == null ||
                itemRequest.getDescription().isEmpty() ||
                itemRequest.getDescription().isBlank()) {
            log.error("Описание запроса не может быть пустым: {}", itemRequest);
            throw new ValidationException("Описание запроса отсутствует");
        }

        var now = LocalDateTime.now();

        itemRequest.setUserId(checkUser(userId).getId());
        itemRequest.setCreated(now);
        itemRequest = itemRequestRepository.save(itemRequest);
        log.info("Запрос был сохранен в базу {}", itemRequest);
        return itemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> getAllRequestsByUser(Long userId) throws ValidationException {
        User user = checkUser(userId);

        List<ItemRequest> requests = itemRequestRepository.findAllByUserId(user.getId());

        List<ItemRequestDto> requestsDto = requests.stream()
                .map(itemRequestMapper :: toItemRequestDto)
                .collect(Collectors.toList());

        return requestsDto;
    }

    @Override
    public List<ItemRequestDto> getAllRequests(Long userId, Integer from, Integer size) throws ValidationException {
        checkUser(userId);

        if(from < 0 ){
            throw new ValidationException("Индекс первого элемента не может быть меньше нуля");
        }

//        Pagination pagination = new Pagination(from, size);
//        Page<ItemRequestDto> itemRequests = itemRequestRepository.findAllByCreated(PageRequest.of(from, size));
//        List<ItemRequestDto> itemRequests = itemRequestRepository.findAllByCreated();
//        return itemRequests.getContent();
//        return itemRequests;
        return null;
    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователь не найден"));
    }

}
