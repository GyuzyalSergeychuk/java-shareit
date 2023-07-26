package ru.practicum.shareit.booking.model;

import lombok.*;
import org.apache.coyote.Request;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "booking")
@Setter @Getter @ToString
@NoArgsConstructor @AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "booker_id")
    private Long bookerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id) &&
                Objects.equals(startDate, booking.startDate) &&
                Objects.equals(endDate, booking.endDate) &&
                Objects.equals(itemId, booking.itemId) &&
                Objects.equals(bookerId, booking.bookerId) &&
                status == booking.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startDate, endDate, itemId, bookerId, status);
    }
}
