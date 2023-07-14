package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class Item {
    private static Long nextId = 0L;
    private Long id;
    private Long userId;
    private String name;
    private String description;
    private Boolean available;
    private String owner;
    private String request;

    public void assignId() {
        nextId++;
        id = nextId;
    }
}
