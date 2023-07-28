package ru.practicum.shareit.item.model;

import antlr.collections.List;
import lombok.*;
import org.apache.coyote.Request;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

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
    private Boolean isAvailable;

    @Column(name = "user_id")
    private Long ownerId;

    @Column(name = "request_id")
    private Long request;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id) &&
                Objects.equals(name, item.name) &&
                Objects.equals(description, item.description) &&
                Objects.equals(isAvailable, item.isAvailable) &&
                Objects.equals(ownerId, item.ownerId) &&
                Objects.equals(request, item.request);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, isAvailable, ownerId, request);
    }
}
