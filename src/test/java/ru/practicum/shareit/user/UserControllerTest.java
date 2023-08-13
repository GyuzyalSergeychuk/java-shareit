package ru.practicum.shareit.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.services.UserServices;

import java.nio.charset.StandardCharsets;

import org.hamcrest.Matchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static ru.practicum.shareit.data.DataFactory.getUserDto;

import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserServices userServices;

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
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(Matchers.containsString(userDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(Matchers.containsString(userDto.getEmail())));
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
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(Matchers.containsString(userDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(Matchers.containsString(userDto.getEmail())));
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
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1));
    }

    @Test
    void getUserId() {
    }

    @Test
    void deleteUser() {
    }
}