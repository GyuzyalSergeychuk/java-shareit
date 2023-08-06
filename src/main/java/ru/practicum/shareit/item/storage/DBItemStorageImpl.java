package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.booking.dto.BookingForGetItemDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.сomment.Comment;
import ru.practicum.shareit.item.сomment.CommentDto;
import ru.practicum.shareit.item.сomment.CommentMapper;
import ru.practicum.shareit.item.сomment.CommentRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.storage.DBUserStorageImpl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public ItemDto create(Long userId, Item item) throws ValidationException, ObjectNotFoundException {
        Item afterCheckItem = standardCheck(item);
        userService.getUserById(userId);
        afterCheckItem.setOwnerId(userId);
        Item itemBase = itemRepository.save(afterCheckItem);
        log.info("вещь успешно добавлен {}", itemBase.getId());
        return itemMapper.toItemDto(itemBase);
    }

    @Override
    public ItemDto update(Long userId, Long itemId, Item itemReq) {
        if (itemId <= 0) {
            throw new ObjectNotFoundException("вещь не найдена");
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
        log.info("Изменения вещи {} успешно внесены", item.getId());
        return itemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getAllItemsDto(Long userId) {
        userService.getUserById(userId);
        List<Item> itemList = itemRepository.findByOwnerIdOrderByNextBookingIdAsc(userId);
        return itemList.stream()
                .map(e -> {
                    ItemDto itemDto = itemMapper.toItemDto(e);
                    setBookingsIntoItemDto(itemDto);
                    return itemDto;
                })
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
                new ObjectNotFoundException("Вещь не найдена"));

        ItemDto itemDto = itemMapper.toItemDto(item);

        if (itemDto.getOwnerId().equals(userId)) {
            itemDto = setBookingsIntoItemDto(itemDto);
        }
        return itemDto;
    }

    @Override
    public Item getItemById(Long itemId) {
        if (itemId <= 0) {
            throw new ObjectNotFoundException("Id не может быть меньше нуля");
        }

        return itemRepository.findById(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Вещь не найдена"));
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

    @Override
    public CommentDto createComment(Long userId, Long itemId, Comment comment) throws ValidationException {
        userRepository.findById(userId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователь не найден"));
        itemRepository.findById(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Вещь не найдена"));
        Booking booking = bookingRepository.findById(userId).get();

        if (comment.getText() == null || comment.getText().isEmpty() || comment.getText().isBlank()){
            throw new ValidationException("Текст комментария не может быть пустым");
        }

        if (booking.getBookerId().equals(userId) && booking.getItemId().equals(itemId)){
            comment.setAuthorId(userId);
            comment.setCreated(LocalDateTime.now());
            comment.setItemId(itemId);
        } else {
            log.info("Пользователь {} не может оставить комментарий, так как не бронировал вещь {}", userId, itemId);
            throw new ValidationException("Не возможно добавить комментарий");
        }

        comment = commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }


    private ItemDto setBookingsIntoItemDto(ItemDto itemDto) {

        List<Booking> allBookingsForCurrentItem = bookingRepository.findByItemIdOrderByStartDesc(itemDto.getId());
        List<Booking> sortingBooking = allBookingsForCurrentItem.stream()
//                        .filter(e -> e.getEnd().isBefore(LocalDateTime.now()))
                .filter(e -> e.getStatus().equals(Status.APPROVED))
                .sorted(Comparator.comparing(Booking::getStart))
                .collect(Collectors.toList());
        if (sortingBooking.isEmpty()) {
            return itemDto;
        } else {
            LocalDateTime now = LocalDateTime.now();
            Booking nearestLastBooking = sortingBooking.get(0);
            Booking nearestNextBooking= sortingBooking.get(0);
            int count = 0;
            for (Booking booking :sortingBooking) {
                if (booking.getEnd().isBefore(now) && booking.getEnd().isAfter(nearestLastBooking.getEnd())) {
                    nearestLastBooking = booking;
                }
                if (booking.getStart().isAfter(now) && count == 0) {
                    nearestNextBooking = booking;
                    count += 1;
                }
                if (booking.getStart().isAfter(now) && booking.getStart().isBefore(nearestNextBooking.getStart())) {
                    nearestNextBooking = booking;
                }
            }
            itemDto.setLastBooking(mapBookingForGetItemDto(nearestLastBooking.getId()));
            itemDto.setNextBooking(mapBookingForGetItemDto(nearestNextBooking.getId()));
        }
        return itemDto;
    }

    // Маппим букинг в мелкий букинг для возврата внутри итема
    private BookingForGetItemDto mapBookingForGetItemDto(Long bookingId) {
        Booking booking = bookingRepository.getById(bookingId);
        return BookingForGetItemDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBookerId())
                .build();
    }

    private Item standardCheck(Item item) throws ValidationException {
        if (item.getName() == null ||
                item.getName().isEmpty() ||
                item.getName().isBlank()) {
            log.error("Название товара не может быть пустым: {}", item);
            throw new ValidationException("Неверно указано название вещи");
        }
        if (item.getDescription() == null ||
                item.getDescription().isEmpty() ||
                item.getDescription().isBlank()) {
            log.error("Неверно введено описание вещи: {}", item);
            throw new ValidationException("Неверно указано описание вещи");
        }
        if (item.getAvailable() == null) {
            throw new ValidationException("Неверно указано описание вещи");
        }
        return item;
    }
}
