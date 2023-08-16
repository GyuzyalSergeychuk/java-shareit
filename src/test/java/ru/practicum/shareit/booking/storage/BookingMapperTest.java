package ru.practicum.shareit.booking.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.data.DataFactory.*;

@ExtendWith(MockitoExtension.class)
public class BookingMapperTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private ItemMapper itemMapper;
    @InjectMocks
    private BookingMapper bookingMapper;

    @Test
    void toBookingDtoTest() {
        var now = LocalDateTime.now();
        var itemDto = getItemDto(1L, "dd", "ff", true, 2L);
        var item = getItem(1L, "dd", "ff", true, 2L);
        var userDto = getUserDto(1L, "dd", "ff");
        var user = getUser(1L, "dd", "ff");
        var booking = getBooking(1L,
                now,
                now,
                1L,
                1L,
                Status.APPROVED,
                1L);
        var bookingDto = getBookingDto(1L,
                now,
                now,
                itemDto,
                userDto,
                Status.APPROVED,
                1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(userDto);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);
        var actualResp = bookingMapper.toBookingDto(booking);

        assertEquals(bookingDto, actualResp);
    }

    @Test
    void toBookingDtoconverNullTest() {
        var now = LocalDateTime.now();
        var booking = getBooking(1L,
                now,
                now,
                1L,
                1L,
                Status.APPROVED,
                1L);
        var bookingDto = getBookingDto(1L,
                now,
                now,
                null,
                null,
                Status.APPROVED,
                1L);

        when(userRepository.findById(any())).thenReturn(Optional.empty());
        when(itemRepository.findById(any())).thenReturn(Optional.empty());

        var actualResp = bookingMapper.toBookingDto(booking);

        assertEquals(bookingDto, actualResp);
    }
}
