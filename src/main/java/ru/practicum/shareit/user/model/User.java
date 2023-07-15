package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Data
@Builder
public class User {
    private Long id;
    private String name;
    private String email;
    private List<Item> items;
}
