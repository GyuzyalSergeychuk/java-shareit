package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.booking.dto.BookingForGetItemDto;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
public class ItemDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NonNull
    private Boolean available;
    private Long ownerId;
    private BookingForGetItemDto lastBooking;
    private BookingForGetItemDto nextBooking;
    private List<CommentDto> comments;
    private Long requestId;
}
