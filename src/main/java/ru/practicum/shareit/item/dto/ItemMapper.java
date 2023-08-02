package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.storage.DBBookingStorageImpl;
import ru.practicum.shareit.item.model.Item;

@Component
@RequiredArgsConstructor
public class ItemMapper {
    private final DBBookingStorageImpl dbBookingStorage;

    public ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwnerId())
                .lastBooking(dbBookingStorage.getBookingDtoById(item.getOwnerId(), item.getLastBookingId()))
                .nextBooking(dbBookingStorage.getBookingDtoById(item.getOwnerId(), item.getNextBookingId()))
//                .request(item.getRequest())
                .build();
    }
}
