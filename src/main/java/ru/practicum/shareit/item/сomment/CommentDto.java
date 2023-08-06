package ru.practicum.shareit.item.сomment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDto {
    private Long id;
    private String text;
    private Long userId;
}
