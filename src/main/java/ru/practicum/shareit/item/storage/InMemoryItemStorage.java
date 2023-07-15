package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.storage.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class InMemoryItemStorage implements ItemStorage {

    private static Long nextId = 0L;
    private final ItemMapper itemMapper;
    private final InMemoryUserStorage inMemoryUserStorage;
    private  List<Item> items = new ArrayList<>();

    @Override
    public ItemDto create(Long userId, Item item) throws ValidationException {
        Long user1 = inMemoryUserStorage.getUserId(userId).getId();
        Item afterCheckItem = standardCheck(item);
        afterCheckItem.setId(assignId());
        item.setUserId(user1);
        items.add(item);
        UserDto userDto = inMemoryUserStorage.getUserId(user1);
        if (userDto.getItems() == null) {
            userDto.setItems(new ArrayList<Item>());
            userDto.getItems().add(item);
        } else {
            userDto.getItems().add(item);
        }
        ItemDto itemDto = itemMapper.toItemDto(item);
        log.info("Товар успешно добавлен {}", itemDto.getId());
        return itemDto;
    }

    @Override
    public ItemDto update(Long userId, Long itemId, Item itemReq) {
        if (itemId <= 0) {
            throw new ObjectNotFoundException("Товар не найден");
        }
        inMemoryUserStorage.getUserId(userId);
        for (Item item : items) {
            if (item.getId().equals(itemId) && !(item.getUserId().equals(userId))) {
                throw new ObjectNotFoundException("Юзер пытается редактировать чужой товар");
            }
        }
        itemReq.setUserId(userId);
        Optional<Item> item = items.stream()
                .filter(e -> e.getId().equals(itemId))
                .peek(e -> {
                    if (itemReq.getName() != null) {
                        e.setName(itemReq.getName());
                    }
                    if (itemReq.getDescription() != null) {
                        e.setDescription(itemReq.getDescription());
                    }
                    if (itemReq.getAvailable() != null) {
                        e.setAvailable(itemReq.getAvailable());
                    }
                })
                .findFirst();
        ItemDto itemDto = itemMapper.toItemDto(item.get());
        log.info("Изменения товара {} успешно внесены", itemDto.getId());
        return itemDto;
    }

    @Override
    public List<ItemDto> getFindAllItems(Long userId) {
        UserDto userDto = inMemoryUserStorage.getUserId(userId);
        return items.stream()
                .filter(e -> e.getUserId().equals(userDto.getId()))
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemId(Long itemId) {
        if (itemId <= 0) {
            throw new ObjectNotFoundException("Товар не найден");
        }
        Optional<Item> item = items.stream()
                .filter(e -> e.getId().equals(itemId))
                .findFirst();
        return itemMapper.toItemDto(item.get());
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        List<ItemDto> findItems = new ArrayList<>();
        if (text == null || text.isEmpty() || text.isBlank()) {
            return findItems;
        }
        String refactorText = text.toLowerCase();
        for (Item item : items) {
            if (item.getAvailable()) {
                if (item.getName().toLowerCase().contains(refactorText)) {
                    findItems.add(itemMapper.toItemDto(item));
                } else if (item.getDescription().toLowerCase().contains(refactorText)) {
                    findItems.add(itemMapper.toItemDto(item));
                }
            }
        }
        return findItems;
    }

    private Item standardCheck(Item item) throws ValidationException {
        if (item.getName() == null ||
                item.getName().isEmpty() ||
                item.getName().isBlank() ||
                item.getName().contains(" ")) {
            log.error("Название товара не может быть пустым: {}", item);
            throw new ValidationException("Неверно указано название товара");
        }
        if (item.getDescription() == null ||
                item.getDescription().isEmpty() ||
                item.getDescription().isBlank()) {
            log.error("Неверно введено описание товара: {}", item);
            throw new ValidationException("Неверно указано описание товара");
        }
        if (item.getAvailable() == null) {
            throw new ValidationException("Неверно указано описание товара");
        }
        return item;
    }

    public static Long assignId() {
        nextId++;
        return nextId;
    }
}
