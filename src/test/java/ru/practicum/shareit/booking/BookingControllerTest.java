package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.services.BookingServices;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.data.DataFactory.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingServices bookingServices;

    @Autowired
    private MockMvc mvc;

    @Test
    void createTest() throws Exception {
        String start = "2003-08-14 00:10:00";
        String end = "2003-08-16 12:10:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime createdStart = LocalDateTime.parse(start, formatter);
        LocalDateTime createdEnd = LocalDateTime.parse(end, formatter);
        var bookingDto = getBookingDto(1L, createdStart, createdEnd, null, null, Status.APPROVED, 1L);

        when(bookingServices.create(anyLong(), any())).thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.start").value(containsString(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$.end").value(containsString(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$.item").value(bookingDto.getItem()))
                .andExpect(jsonPath("$.booker").value(bookingDto.getBooker()))
                .andExpect(jsonPath("$.status").value(bookingDto.getStatus().toString()))
                .andExpect(jsonPath("$.itemOwner").value(bookingDto.getItemOwner()));
    }

    @Test
    void approvedTest() throws Exception {
        String start = "2003-08-14 00:10:00";
        String end = "2003-08-16 12:10:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime createdStart = LocalDateTime.parse(start, formatter);
        LocalDateTime createdEnd = LocalDateTime.parse(end, formatter);
        var bookingDto = getBookingDto(1L, createdStart, createdEnd, null, null, Status.APPROVED, 1L);

        when(bookingServices.approved(any(), any(), any())).thenReturn(bookingDto);

        mvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.start").value(containsString(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$.end").value(containsString(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$.item").value(bookingDto.getItem()))
                .andExpect(jsonPath("$.booker").value(bookingDto.getBooker()))
                .andExpect(jsonPath("$.status").value(bookingDto.getStatus().toString()))
                .andExpect(jsonPath("$.itemOwner").value(bookingDto.getItemOwner()));
    }

    @Test
    void getBookingIdTest() throws Exception {
        String start = "2003-08-14 00:10:00";
        String end = "2003-08-16 12:10:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime createdStart = LocalDateTime.parse(start, formatter);
        LocalDateTime createdEnd = LocalDateTime.parse(end, formatter);
        var bookingDto = getBookingDto(1L, createdStart, createdEnd, null, null, Status.APPROVED, 1L);

        when(bookingServices.getBookingId(any(), any())).thenReturn(bookingDto);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.start").value(containsString(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$.end").value(containsString(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$.item").value(bookingDto.getItem()))
                .andExpect(jsonPath("$.booker").value(bookingDto.getBooker()))
                .andExpect(jsonPath("$.status").value(bookingDto.getStatus().toString()))
                .andExpect(jsonPath("$.itemOwner").value(bookingDto.getItemOwner()));
    }

    @Test
    void getAllBookingsByUserTest() throws Exception {
        String start = "2003-08-14 00:10:00";
        String end = "2003-08-16 12:10:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime createdStart = LocalDateTime.parse(start, formatter);
        LocalDateTime createdEnd = LocalDateTime.parse(end, formatter);
        var bookingDto = getBookingDto(1L, createdStart, createdEnd, null, null, Status.APPROVED, 1L);
        var bookings = List.of(bookingDto);

        when(bookingServices.getAllBookingsByUser(any(), any(), any(), any())).thenReturn(bookings);

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "APPROVED")
                        .param("from", "0")
                        .param("size", "20")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1L))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].start").value(containsString(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$[0].end").value(containsString(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$[0].item").value(bookingDto.getItem()))
                .andExpect(jsonPath("$[0].booker").value(bookingDto.getBooker()))
                .andExpect(jsonPath("$[0].status").value(bookingDto.getStatus().toString()))
                .andExpect(jsonPath("$[0].itemOwner").value(bookingDto.getItemOwner()));
    }

    @Test
    void getAllBookingsByItemsTest() throws Exception {
        String start = "2003-08-14 00:10:00";
        String end = "2003-08-16 12:10:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime createdStart = LocalDateTime.parse(start, formatter);
        LocalDateTime createdEnd = LocalDateTime.parse(end, formatter);
        var bookingDto = getBookingDto(1L, createdStart, createdEnd, null, null, Status.APPROVED, 1L);
        var bookings = List.of(bookingDto);

        when(bookingServices.getAllBookingsByItems(any(), any(), any(), any())).thenReturn(bookings);

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "APPROVED")
                        .param("from", "0")
                        .param("size", "20")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1L))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].start").value(containsString(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$[0].end").value(containsString(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$[0].item").value(bookingDto.getItem()))
                .andExpect(jsonPath("$[0].booker").value(bookingDto.getBooker()))
                .andExpect(jsonPath("$[0].status").value(bookingDto.getStatus().toString()))
                .andExpect(jsonPath("$[0].itemOwner").value(bookingDto.getItemOwner()));
    }
}