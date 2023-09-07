package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.services.UserServices;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.data.DataFactory.getUserDto;

import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserServices userServices;

    @Autowired
    private MockMvc mvc;


    @Test
    void create() throws Exception {
        var userDto = getUserDto(1L, "user", "user@user.com");

        when(userServices.create(any())).thenReturn(userDto);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(containsString(userDto.getName())))
                .andExpect(jsonPath("$.email").value(containsString(userDto.getEmail())));
    }

    @Test
    void update() throws Exception {
        var userDto = getUserDto(1L, "user", "user@user.com");

        when(userServices.update(any(), any())).thenReturn(userDto);

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(containsString(userDto.getName())))
                .andExpect(jsonPath("$.email").value(containsString(userDto.getEmail())));
    }

    @Test
    void getFindAllUsers() throws Exception {
        var userDto = getUserDto(1L, "user", "user@user.com");
        var users = List.of(userDto);

        when(userServices.getFindAllUsers()).thenReturn(users);

        mvc.perform(get("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value(containsString(userDto.getName())))
                .andExpect(jsonPath("$[0].email").value(containsString(userDto.getEmail())));
    }

    @Test
    void getUserId() throws Exception {
        var userDto = getUserDto(1L, "user", "user@user.com");

        when(userServices.getUserId(any())).thenReturn(userDto);

        mvc.perform(get("/users/1")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(containsString(userDto.getName())))
                .andExpect(jsonPath("$.email").value(containsString(userDto.getEmail())));
    }

    @Test
    void deleteUser() throws Exception {
        var id = 1L;
        var userDto = getUserDto(1L, "user", "user@user.com");

        when(userServices.deleteUser(id)).thenReturn(true);

        mvc.perform(delete("/users/1")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}