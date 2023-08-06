package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingForGetItemDto;
import ru.practicum.shareit.item.сomment.CommentDto;

import java.util.List;
/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
    private BookingForGetItemDto lastBooking;
    private BookingForGetItemDto nextBooking;
    private List<CommentDto> comments;
//    private Request request;
}
