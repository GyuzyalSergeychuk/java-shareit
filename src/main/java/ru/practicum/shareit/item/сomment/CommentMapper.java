package ru.practicum.shareit.item.сomment;

import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    private CommentDto toCommentDto(Comment comment){
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .userId(comment.getUserId())
                .build();
    }
}
