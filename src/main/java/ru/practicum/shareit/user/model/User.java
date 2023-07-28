package ru.practicum.shareit.user.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;


import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "user_name", length = 64, nullable = false)
    private String name;

    @Column(name = "email", length = 128, nullable = false)
    private String email;

    @OneToMany
    private List<Item> items;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(name, user.name) &&
//                Objects.equals(email, user.email) &&
                Objects.equals(email, user.email) &&
                Objects.equals(items, user.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, items);
    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(id, name, email);
//    }
}
