package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingResponseDto {
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemResponseDto item;
}
