package ru.practicum.shareit.item.storage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.data.DataFactory;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.storage.DBUserStorageImpl;

import java.util.Optional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.data.DataFactory.*;

@ExtendWith(MockitoExtension.class)
class DBItemStorageImplTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private DBUserStorageImpl userService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentMapper commentMapper;
    @InjectMocks
    private DBItemStorageImpl dbItemStorage;

//    @Test
//    void getItemDtoById() {
//        // arrange
//        var itemId = 1L;
//        var userId = 2L;
//        var item = getItem(userId, itemId);
//        var itemDto = getItemDto(userId, itemId);
//        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
//        Mockito.when(itemMapper.toItemDto(item)).thenReturn(itemDto);
//        Mockito.when(bookingRepository.findByItemIdOrderByStartDesc(itemDto.getId()))
//                .thenReturn(List.of(getBooking(1L, 1L, 2L )));

//        // act
//        var actualResponse = dbItemStorage.getItemDtoById(userId, itemId);
//
//        // assert
//        assertEquals(itemDto, actualResponse);
//        verify(itemRepository, times(1)).findById(anyLong());
//    }
}