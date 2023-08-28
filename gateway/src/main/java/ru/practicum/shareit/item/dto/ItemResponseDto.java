package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingForGetItemDto;

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
    private List<CommentDto> comments;
    private Long requestId;
}
