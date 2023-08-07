package ru.practicum.shareit.request.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

/**
 * TODO Sprint add-item-requests.
 * Работа с ItemRequestController начнется только с 15ТЗ
 */

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
