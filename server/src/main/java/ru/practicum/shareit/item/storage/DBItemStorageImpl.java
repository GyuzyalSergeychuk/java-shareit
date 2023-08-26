package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.booking.dto.BookingForGetItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.pagination.Pagination;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.storage.DBUserStorageImpl;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DBItemStorageImpl implements ItemStorage {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final DBUserStorageImpl userService;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final Pagination<Item> pagination;

    @Override
    public ItemDto create(Long userId, Item item) throws ValidationException, ObjectNotFoundException {
        userService.getUserById(userId);
        item.setOwnerId(userId);
        Item itemBase = itemRepository.save(item);
        log.info("вещь успешно добавлен {}", itemBase.getId());
        return itemMapper.toItemDto(itemBase);
    }

    @Override
    public ItemDto update(Long userId, Long itemId, Item itemReq) {
        userService.getUserById(userId);

        Item itemBase = itemRepository.findByIdAndOwnerId(itemId, userId);

        if (itemBase == null) {
            throw new ObjectNotFoundException("Юзер пытается редактировать чужой товар");
        }

        if (itemReq.getName() != null) {
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
    public List<ItemDto> getAllItemsDto(Long userId, Integer from, Integer size) throws ValidationException {
        userService.getUserById(userId);
        List<Item> itemList = itemRepository.findByOwnerIdOrderByNextBookingIdDesc(userId);
        List<Item> list;
        if (from == null && size == null) {
            list = pagination.makePagination(0, 20, itemList);
        } else if (from > -1 && size > 0) {
            list = pagination.makePagination(from, size, itemList);
        } else {
            throw new ValidationException("from and size не могут быть меньше нулями");
        }
        return list.stream()
                .map((Item e) -> {
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
    public ItemDto getItemDtoById(Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Вещь не найдена"));

        ItemDto itemDto = itemMapper.toItemDto(item);

        if (itemDto.getOwnerId().equals(userId)) {
            itemDto = setBookingsIntoItemDto(itemDto);
        }
        return itemDto;
    }

    private ItemDto setBookingsIntoItemDto(ItemDto itemDto) {
        List<Booking> allBookingsForCurrentItem = bookingRepository.findByItemIdOrderByStartDesc(itemDto.getId());
        List<Booking> sortingBooking = allBookingsForCurrentItem.stream()
                .filter((Booking e) -> e.getStatus().equals(Status.APPROVED))
                .sorted(Comparator.comparing(Booking::getStart))
                .collect(Collectors.toList());
        if (sortingBooking.isEmpty()) {
            return itemDto;
        } else {
            LocalDateTime now = LocalDateTime.now();
            Booking nearestLastBooking = sortingBooking.get(0);
            Booking nearestNextBooking = sortingBooking.get(0);
            int count = 0;
            for (Booking booking : sortingBooking) {
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
            if (count == 1) {
                itemDto.setNextBooking(mapBookingForGetItemDto(nearestNextBooking.getId()));
            } else {
                itemDto.setNextBooking(null);
            }
        }
        return itemDto;
    }

    @Override
    public List<ItemDto> searchItem(String text, Integer from, Integer size) throws ValidationException {
        String refactorText = text.toLowerCase();
        String text1 = StringUtils.capitalize(refactorText);

        List<Item> items = itemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(text1, text1);
        List<Item> list;

        if (from == null && size == null) {
            list = pagination.makePagination(0, 20, items);
        } else if (from > -1 && size > 0) {
            list = pagination.makePagination(from, size, items);
        } else {
            throw new ValidationException("from and size не могут быть нулями");
        }

        return list.stream()
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

        if (booking.getBookerId().equals(userId) && booking.getItemId().equals(itemId)) {
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

    // Маппим букинг в мелкий букинг для возврата внутри итема
    private BookingForGetItemDto mapBookingForGetItemDto(Long bookingId) {
        Booking booking = bookingRepository.getById(bookingId);
        return BookingForGetItemDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBookerId())
                .build();
    }
}
