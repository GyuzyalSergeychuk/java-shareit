package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.сomment.Comment;
import ru.practicum.shareit.item.сomment.CommentDto;
import ru.practicum.shareit.item.сomment.CommentMapper;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemMapper {

    private final CommentMapper commentMapper;

    public ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwnerId())
                .comments(mapComments(item.getComments()))
                .build();
    }

    private List<CommentDto> mapComments(List<Comment> comments) {
        if (comments == null) {
            return List.of();
        }
        return comments.stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
