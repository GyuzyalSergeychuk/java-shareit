package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Data
@Builder
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private List<Item> items;
}
