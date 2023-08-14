package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.shareit.data.DataFactory.getUser;
import static ru.practicum.shareit.data.DataFactory.getUserDto;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @InjectMocks
    private UserMapper userMapper;

    @Test
    void toUserDto() {
        var user = getUser(1L, "user", "user@user.com");
        var userDto = getUserDto(1L, "user", "user@user.com");

        var actualResp = userMapper.toUserDto(user);

        assertEquals(userDto, actualResp);
    }
}