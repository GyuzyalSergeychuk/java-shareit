package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.data.DataFactory.*;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ItemMapperTest {

    @Mock
    private CommentMapper commentMapper;
    @InjectMocks
    private ItemMapper itemMapper;

    @Test
    void toItemDto() {
        String start = "2003-08-14 00:10:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime created = LocalDateTime.parse(start, formatter);
        var comment = new Comment();
        var comments = List.of(comment);
        var commentDto = CommentDto.builder().build();
        var commentsDto = List.of(commentDto);
        var item = getItemBooking(1L,
                "Дрель",
                "Простая дрель",
                true,
                4L,
                null,
                null,
                null,
                comments);
        var itemDto = getItemDtoBooking(1L,
                "Дрель",
                "Простая дрель",
                true,
                4L,
                null,
                null,
                commentsDto,
                null);

        when(commentMapper.toCommentDto(comment)).thenReturn(commentDto);
        var actualResp = itemMapper.toItemDto(item);

        assertEquals(itemDto, actualResp);
    }
}