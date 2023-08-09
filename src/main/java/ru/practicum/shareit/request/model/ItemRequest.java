package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.DoubleStream;

@Entity
@Table(name = "requests")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", length = 200, nullable = false)
    private String description;

    @Column(name = "created_date")
    private LocalDateTime created;

    @Column(name = "user_id")
    private Long userId;

    @OneToMany
    @JoinColumn(name = "request_id", referencedColumnName = "id")
    private List<Item> items;

}
