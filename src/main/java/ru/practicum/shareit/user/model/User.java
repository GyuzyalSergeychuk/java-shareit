package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class User {
    private static Long nextId = 0L;
    private Long id;
    private String name;
    private String email;

    public void assignId() {
        nextId++;
        id = nextId;
    }
}
