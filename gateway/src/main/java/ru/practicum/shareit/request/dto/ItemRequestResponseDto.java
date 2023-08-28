package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class ItemRequestResponseDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private Long userId;
    private List<ItemResponseDto> items;
}
