package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, final Status status);

    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findByItemIdOrderByStartDesc(Long itemId);

    List<Booking> findByItemIdAndStatusOrderByStartDesc(Long itemId, Status status);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long bookerId,
                                                                          LocalDateTime before,
                                                                          LocalDateTime after);

    List<Booking> findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long userId,
                                                                             LocalDateTime before,
                                                                             LocalDateTime after);

    List<Booking> findByBookerIdAndEndBeforeAndStatusEqualsOrderByStartDesc(Long userId,
                                                                            LocalDateTime before,
                                                                            final Status status);

    List<Booking> findByItemOwnerIdAndEndBeforeAndStatusEqualsOrderByStartDesc(Long userId,
                                                                               LocalDateTime before,
                                                                               final Status status);

}