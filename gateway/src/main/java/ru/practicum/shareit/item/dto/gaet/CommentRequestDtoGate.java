package ru.practicum.shareit.item.dto.gaet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentRequestDtoGate {
    private String text;

    @JsonCreator
    public CommentRequestDtoGate(@JsonProperty String text) {
        this.text = text;
    }
}
