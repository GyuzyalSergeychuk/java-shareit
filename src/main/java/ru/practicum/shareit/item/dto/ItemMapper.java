package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.storage.DBBookingStorageImpl;
import ru.practicum.shareit.item.model.Item;

@Component

public class ItemMapper {
    private final DBBookingStorageImpl dbBookingStorage;

    public ItemMapper (@Lazy DBBookingStorageImpl dbBookingStorage) {
        this.dbBookingStorage = dbBookingStorage;
    }

    public ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwnerId())
                .build();
    }
}
