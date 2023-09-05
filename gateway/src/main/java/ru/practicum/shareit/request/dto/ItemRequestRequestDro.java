package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemRequestRequestDro {
    private String description;

    @JsonCreator
    public ItemRequestRequestDro(@JsonProperty String description) {
        this.description = description;
    }
}
