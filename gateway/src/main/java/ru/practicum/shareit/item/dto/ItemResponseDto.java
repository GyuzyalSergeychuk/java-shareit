package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingForGetItemDto;
import ru.practicum.shareit.item.dto.gaet.CommentDtoGate;

import java.util.List;

@Data
@Builder
public class ItemResponseDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
    private BookingForGetItemDto lastBooking;
    private BookingForGetItemDto nextBooking;
    private List<CommentDtoGate> comments;
    private Long requestId;
}
