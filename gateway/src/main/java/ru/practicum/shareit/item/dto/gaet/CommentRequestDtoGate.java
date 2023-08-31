package ru.practicum.shareit.item.dto.gaet;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentRequestDtoGate {
    private String text;
}
