package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForGetItemDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.storage.DBUserStorageImpl;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Primary
public class DBItemStorageImpl implements ItemStorage {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final DBUserStorageImpl userService;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public ItemDto create(Long userId, Item item) throws ValidationException, ObjectNotFoundException {
        Item afterCheckItem = standardCheck(item);
        userService.getUserById(userId);
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
        userService.getUserById(userId);

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
    public List<ItemDto> getAllItemsDto(Long userId) {
        userService.getUserById(userId);
        List<Item> itemList = itemRepository.findByOwnerId(userId);
        return itemList.stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getAllItems(Long userId) {
        userService.getUserById(userId);
        return itemRepository.findByOwnerId(userId);
    }

    @Override
    public ItemDto getItemDtoById(Long userId, Long itemId)  {
        if (itemId <= 0) {
            throw new ObjectNotFoundException("Id не может быть меньше нуля");
        }

        Item item= itemRepository.findById(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователь не найден"));

        ItemDto itemDto = itemMapper.toItemDto(item);

        if (itemDto.getOwnerId().equals(userId)) {
            if (item.getLastBookingId() != null) {
                Booking lastBooking = bookingRepository.getById(item.getLastBookingId());
                Booking nextBooking = bookingRepository.getById(item.getNextBookingId());
                BookingForGetItemDto lastBookingDto = BookingForGetItemDto.builder()
                        .id(lastBooking.getId())
                        .bookerId(lastBooking.getBookerId())
                        .build();
                BookingForGetItemDto nextBookingDto = BookingForGetItemDto.builder()
                        .id(nextBooking.getId())
                        .bookerId(nextBooking.getBookerId())
                        .build();

                itemDto.setLastBooking(lastBookingDto);
                itemDto.setNextBooking(nextBookingDto);
            }
        }

        return itemDto;
    }

    @Override
    public Item getItemById(Long itemId) {
        if (itemId <= 0) {
            throw new ObjectNotFoundException("Id не может быть меньше нуля");
        }

        return itemRepository.findById(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователь не найден"));
    }

    @Override
    public List<ItemDto> searchItem(String text) {

        if (text == null) {
            throw new ObjectNotFoundException("Запрос на поиск товара не может быть пустым");
        }

        if (text.isEmpty() || text.isBlank()){
            return List.of();
        }
        String refactorText = text.toLowerCase();
        String text1 = StringUtils.capitalize(refactorText);

        List<Item> items = itemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(text1, text1);
        return items.stream()
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
