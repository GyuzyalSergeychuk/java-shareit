package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.data.DataFactory.getComment;
import static ru.practicum.shareit.data.DataFactory.getCommentDto;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentMapper commentMapper;

    @Test
    void toCommentDtoTest() {
        var user = new User();
        user.setName("user");
        String str = "2003-08-14 00:10:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime created = LocalDateTime.parse(str, formatter);
        var comment = getComment(1L, "Кухонный стол", 1L, created, 1L);
        var commentDto = getCommentDto(1L, "Кухонный стол", "user", created);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        var actual = commentMapper.toCommentDto(comment);

        assertEquals(commentDto, actual);
    }
}