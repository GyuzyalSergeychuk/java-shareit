package ru.practicum.shareit.exceptions.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void getError() {

        ErrorResponse errorResponse = new ErrorResponse("Ошибка");

        assertEquals("Ошибка", errorResponse.getError());
    }
}