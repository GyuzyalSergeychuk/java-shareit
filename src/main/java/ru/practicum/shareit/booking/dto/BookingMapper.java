package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class BookingMapper {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

    public BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(convertItemToItemDto(booking.getItemId()))
                .booker(convertBookingToBookingDto(booking.getBookerId()))
                .status(booking.getStatus())
                .itemOwner(booking.getItemOwnerId())
                .build();
    }

    private UserDto convertBookingToBookingDto(Long bookerId) {
        User user = userRepository.findById(bookerId).orElse(null);
        if (user != null) {
            return userMapper.toUserDto(user);
        } else {
            return null;
        }
    }

    private ItemDto convertItemToItemDto(Long itemId) {
        Item item = itemRepository.findById(itemId).orElse(null);
        if (item != null) {
            return itemMapper.toItemDto(item);
        } else {
            return null;
        }
    }
}
