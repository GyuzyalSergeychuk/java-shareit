package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
public class ItemDto {
    private Long id;
    private Long userId;
    private String name;
    private String description;
    private Boolean available;
    private String owner;
    private String request;
}
