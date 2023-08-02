package ru.practicum.shareit.item.model;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "item")
@Setter @Getter @ToString
@AllArgsConstructor @NoArgsConstructor
@Embeddable
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_name", length = 64, nullable = false)
    private String name;

    @Column(name = "description", length = 200, nullable = false)
    private String description;

    @Column(name = "is_available")
    private Boolean available;

    @Column(name = "user_id")
    private Long ownerId;

    @Column(name = "request_id")
    private Long request;

    @Column(name = "last_booking_id")
    private Long lastBookingId;

    @Column(name = "next_booking_id")
    private Long nextBookingId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id) &&
                Objects.equals(name, item.name) &&
                Objects.equals(description, item.description) &&
                Objects.equals(available, item.available) &&
                Objects.equals(ownerId, item.ownerId) &&
                Objects.equals(request, item.request) &&
                Objects.equals(lastBookingId, item.lastBookingId) &&
                Objects.equals(nextBookingId, item.nextBookingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, available, ownerId, request, lastBookingId, nextBookingId);
    }
}
