package ru.practicum.shareit.item.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.DBStorageImpl;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Primary
public class ItemServiceImpl implements ItemStorage {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final DBStorageImpl userService;


    @Override
    public ItemDto create(Long userId, Item item) throws ValidationException, ObjectNotFoundException {
        Long user1 = userService.getUserId(userId).getId();
        Item afterCheckItem = standardCheck(item);
        afterCheckItem.setOwnerId(user1);
        Item itemBase = itemRepository.save(afterCheckItem);
        ItemDto itemDto = itemMapper.toItemDto(item);
        log.info("Товар успешно добавлен {}", itemDto.getId());
        return itemDto;
    }

    @Override
    public ItemDto update(Long userId, Long itemId, Item itemReq) {

        return null;
    }

    @Override
    public List<ItemDto> getFindAllItems(Long userId) {
        return null;
    }

    @Override
    public ItemDto getItemId(Long itemId) {
        return null;
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        return null;
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
        if (item.getIsAvailable() == null) {
            throw new ValidationException("Неверно указано описание товара");
        }
        return item;
    }
}
