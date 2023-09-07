package ru.practicum.shareit.item.dto.gaet;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDtoGate {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
