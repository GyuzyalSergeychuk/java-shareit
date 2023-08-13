package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.request.services.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.data.DataFactory.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;

    @Test
    void createRequest() throws Exception {
        String str = "2003-08-14 00:10:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime created = LocalDateTime.parse(str, formatter);
        var itemRequestDto = getItemRequestDto(1L,
                "Щётка для обуви",
                created,
                1L, null);
        var itemRequest = getItemRequest(1L,
                "Щётка для обуви",
                created.plusSeconds(0),
                1L, null);

        when(itemRequestService.createRequest(1L, itemRequest)).thenReturn(itemRequestDto);

        mvc.perform(MockMvcRequestBuilders.post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value(containsString(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.created").value(containsString(itemRequestDto.getCreated().toString())))
                .andExpect(jsonPath("$.userId").value(itemRequestDto.getUserId()));
    }

    @Test
    void getAllRequestsByUserTest() throws Exception {
        String str = "2003-08-14 00:10:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime created = LocalDateTime.parse(str, formatter);
        var itemRequestDto = getItemRequestDto(1L,
                "Щётка для обуви",
                created,
                1L, null);
        var itemRequestList = List.of(itemRequestDto);

        when(itemRequestService.getAllRequestsByUser(any())).thenReturn(itemRequestList);

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].description").value(containsString(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$[0].created").value(containsString(itemRequestDto.getCreated().toString())))
                .andExpect(jsonPath("$[0].userId").value(itemRequestDto.getUserId()));
    }

    @Test
    void getAllRequestsTest() throws Exception {
        String str = "2003-08-14 00:10:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime created = LocalDateTime.parse(str, formatter);
        var itemRequestDto = getItemRequestDto(1L,
                "Щётка для обуви",
                created,
                1L, null);
        var itemRequestList = List.of(itemRequestDto);

        when(itemRequestService.getAllRequests(any(), any(), any())).thenReturn(itemRequestList);

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "20")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].description").value(containsString(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$[0].created").value(containsString(itemRequestDto.getCreated().toString())))
                .andExpect(jsonPath("$[0].userId").value(itemRequestDto.getUserId()));
    }

    @Test
    void getRequestsTest() throws Exception {
        String str = "2003-08-14 00:10:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime created = LocalDateTime.parse(str, formatter);
        var itemRequestDto = getItemRequestDto(1L,
                "Щётка для обуви",
                created,
                1L, null);

        when(itemRequestService.getRequests(any(), any())).thenReturn(itemRequestDto);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value(containsString(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.created").value(containsString(itemRequestDto.getCreated().toString())))
                .andExpect(jsonPath("$.userId").value(itemRequestDto.getUserId()));
    }
}