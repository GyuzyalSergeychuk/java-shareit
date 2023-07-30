package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.storage.DBUserStorageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Primary
public class DBItemServiceImpl implements ItemStorage {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final DBUserStorageImpl userService;


    @Override
    public ItemDto create(Long userId, Item item) throws ValidationException, ObjectNotFoundException {
        Item afterCheckItem = standardCheck(item);
        userService.getUserId(userId);
        afterCheckItem.setOwnerId(userId);
        Item itemBase = itemRepository.save(afterCheckItem);
        log.info("Товар успешно добавлен {}", itemBase.getId());
        return itemMapper.toItemDto(itemBase);
    }

    @Override
    public ItemDto update(Long userId, Long itemId, Item itemReq) {
        if (itemId <= 0) {
            throw new ObjectNotFoundException("Товар не найден");
        }
        userService.getUserId(userId);

        Item itemBase = itemRepository.findByIdSelf(itemId, userId);

        if (itemBase == null){
            throw new ObjectNotFoundException("Юзер пытается редактировать чужой товар");
        }

        if(itemReq.getName() != null){
            itemBase.setName(itemReq.getName());
        }
        if (itemReq.getDescription() != null) {
            itemBase.setDescription(itemReq.getDescription());
        }
        if (itemReq.getAvailable() != null) {
            itemBase.setAvailable(itemReq.getAvailable());
        }

        Item item = itemRepository.save(itemBase);
        log.info("Изменения товара {} успешно внесены", item.getId());
        return itemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getFindAllItems(Long userId) {
        userService.getUserId(userId);
        List<Item> itemList = itemRepository.findByOwnerId(userId);
        return itemList.stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemId(Long itemId) {
        if (itemId <= 0) {
            throw new ObjectNotFoundException("Id не может быть меньше нуля");
        }

        Item itemBase= itemRepository.findById(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователь не найден"));

        return itemMapper.toItemDto(itemBase);
    }

    @Override
    public List<ItemDto> searchItem(String text) {

        if (text == null) {
            throw new ObjectNotFoundException("Запрос на поиск товара не может быть пустым");
        }

        if(text.isEmpty() || text.isBlank()){
            List<Item> itemDto = new ArrayList<>();
            return itemDto.stream()
                    .map(itemMapper::toItemDto)
                    .collect(Collectors.toList());
        }
        String refactorText = text.toLowerCase();
        String text1 = StringUtils.capitalize(refactorText);

        List<Item> itemBase = itemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(text1, text1);
        return itemBase.stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private Item standardCheck(Item item) throws ValidationException {
        if (item.getName() == null ||
                item.getName().isEmpty() ||
                item.getName().isBlank()) {
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
}
