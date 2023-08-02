package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;

import java.util.*;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b " +
            "WHERE b.bookerId = :bookerId AND b.status = :status ORDER BY b.start DESC")
    List<Booking> findByBookerIdAndStatusOrderByStartDesc(@Param("bookerId") Long bookerId,
                                                          @Param("status") String status);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.bookerId = :bookerId ORDER BY b.start DESC")
    List<Booking> findByBookerIdOrderByStartDesc(@Param("bookerId") Long bookerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.itemId = :itemId ORDER BY b.start DESC")
    List<Booking> findByItemIdOrderByStartDesc(@Param("itemId") Long itemId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.itemId = :itemId AND b.status = :status ORDER BY b.start DESC")
    List<Booking> findByItemIdAndStatusOrderByStartDesc(@Param("itemId") Long itemId,
                                                        @Param("status") String status);
}