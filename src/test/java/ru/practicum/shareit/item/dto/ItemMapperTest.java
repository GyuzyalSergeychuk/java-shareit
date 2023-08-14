package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.data.DataFactory.*;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ItemMapperTest {

    @Mock
    private CommentMapper commentMapper;
    @InjectMocks
    private ItemMapper itemMapper;

    @Test
    void toItemDtoTest() {
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

    @Test
    void toItemDtoCommentNullTest() {
        List<CommentDto> commentsDto = new ArrayList<>();
        var item = getItem(1L,
                "Дрель",
                "Простая дрель",
                true,
                4L);
        var itemDto = getItemDtoBooking(1L,
                "Дрель",
                "Простая дрель",
                true,
                4L,
                null,
                null,
                commentsDto,
                null);

        var actualResp = itemMapper.toItemDto(item);

        assertEquals(itemDto, actualResp);
    }
}