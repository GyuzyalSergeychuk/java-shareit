package ru.practicum.shareit.request.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "requests")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "description", length = 200, nullable = false)
    private String description;

    @Column(name = "requestor_id")
    private Long requestorId;

    @Column(name = "created_date")
    private Instant created;
}
