package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class InMemoryItemStorage implements ItemStorage {

    private final ItemMapper itemMapper;
    private final InMemoryUserStorage inMemoryUserStorage;
    private Map<Long, List<Item>> items = new HashMap<>();

    @Override
    public ItemDto create(Long userId, Item item) throws ValidationException {
        Long user1 = inMemoryUserStorage.getUserId(userId).getId();
        Item afterCheckItem = standardCheck(item);
        afterCheckItem.assignId();
        if (items.containsKey(user1)) {
            items.get(user1).add(item);
        } else {
            List<Item> listItems = new ArrayList<>();
            listItems.add(item);
            items.put(user1, listItems);
        }
        inMemoryUserStorage.getUserId(user1).getItems().add(item);
        ItemDto itemDto = itemMapper.toItemDto(item);
        itemDto.setUserId(user1);

        log.info("Товар успешно добавлен {}", itemDto.getId());
        return itemDto;
    }

    @Override
    public ItemDto update(Long userId, Item itemReq) {
//        if (item.getId() <= 0) {
//            throw new ObjectNotFoundException("Товар не найдет");
//        }
//        // isEndOfStatusItems - флаг, при false - товар отсутствует, при true - товар можно забронировать
//        boolean isEndOfStatusItems = false;

        if (inMemoryUserStorage.getUserId(userId).equals(null)) {
            throw new ObjectNotFoundException("Пользователь не найдет");
        }
        if (items.containsKey(userId)) {
            List<Item> itemsUser = items.get(userId);
            for (Item item : itemsUser) {
                if (item.getId().equals(itemReq.getId())) {
                    if (itemReq.getName() == null) {
                        itemReq.setName(item.getName());
                    } else if (itemReq.getDescription() == null) {
                        itemReq.setDescription(item.getDescription());
                    } else if (itemReq.getAvailable() == null) {
                        itemReq.setAvailable(item.getAvailable());
                    } else {
                        items.get(userId).add(Math.toIntExact(itemReq.getId()), item);
                    }
                }
            }
        }
            ItemDto itemDto = itemMapper.toItemDto(itemReq);
            itemDto.setUserId(userId);
            log.info("Изменения товара {} успешно внесены", itemDto.getId());
            return itemDto;
        }

        @Override
        public List<ItemDto> getFindAllItems () {
            return items.values().stream()
                    .map(e -> itemMapper.toItemDto((Item) e))
                    .collect(Collectors.toList());
        }

        @Override
        public List<Item> getItemId (Long id){
            if (id <= 0) {
                throw new ObjectNotFoundException("Товар не найдет");
            }
            List<Item> itemDto = items.get(id);
            return itemDto;
        }

        @Override
        public List<ItemDto> itemsAreAvailable (String description){
            if (description.equals(null) || description.isEmpty() || description.isBlank()) {
                throw new ObjectNotFoundException("Товар не найдет");
            }

            List<ItemDto> sortDescriptionItem = new ArrayList<>();

            for (List<Item> value : items.values()) {
                for (Item item : value) {
                     sortDescriptionItem = (items.values()
                            .stream()
                            .filter(e -> item.getDescription().equals(description))
                            .map((List<Item> item1) -> itemMapper.toItemDto((Item) item1))
                            .collect(Collectors.toList()));
                }
            }
            return sortDescriptionItem;
        }

        private Item standardCheck (Item item) throws ValidationException {
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
            if (item.getAvailable() == null || item.getAvailable() != true) {
                item.setAvailable(true);
            }
            return item;
        }
    }
