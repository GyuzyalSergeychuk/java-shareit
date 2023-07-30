package ru.practicum.shareit.booking.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;

@Component
public class BookingMapper {
    public BookingDto toBookingDto(Booking booking){
        return BookingDto.builder()
                .id(booking.getId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .itemId(booking.getItemId())
                .bookerId(booking.getBookerId())
                .status(booking.getStatus())
                .build();
    }
}
