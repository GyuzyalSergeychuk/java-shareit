package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class User {
    private static Long nextId = 0L;
    private Long id;
    private String name;
    private String email;
    private List<Item> items;

    public void assignId() {
        nextId++;
        id = nextId;
    }
}
