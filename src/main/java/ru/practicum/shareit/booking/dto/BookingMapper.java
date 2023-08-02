package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.storage.DBItemStorageImpl;
import ru.practicum.shareit.user.storage.DBUserStorageImpl;

@Component
@RequiredArgsConstructor
public class BookingMapper {

    private final DBUserStorageImpl dbUserStorage;
    private final DBItemStorageImpl dbItemStorage;

    public BookingDto toBookingDto(Booking booking)  {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(dbItemStorage.getItemDtoById(booking.getItemId()))
                .booker(dbUserStorage.getUserDtoById(booking.getBookerId()))
                .status(booking.getStatus())
                .build();
    }
}
