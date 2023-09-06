package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemRequestRequestDto {
    private String description;

    @JsonCreator
    public ItemRequestRequestDto(@JsonProperty String description) {
        this.description = description;
    }
}
