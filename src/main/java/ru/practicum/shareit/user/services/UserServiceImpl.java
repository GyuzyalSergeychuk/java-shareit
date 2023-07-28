package ru.practicum.shareit.user.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Primary
public class UserServiceImpl implements UserStorage {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto create(User user) throws ValidationException, ConflictException {
        User afterCheckUser = standardCheck(user);
        User user1 = userRepository.save(afterCheckUser);
        UserDto userDto = userMapper.toUserDto(user1);
        log.info("Пользователь успешно добавлен {}", user.getId());
        return userDto;
    }

    @Override
    public UserDto update(Long id, User user) throws ValidationException, ConflictException {
        if (id <= 0) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }

        User userBase = userRepository.getById(id);

        if (user.getName() != null) {
            user.setName(userBase.getName());
        } else if (user.getEmail() != null) {
            user.setEmail(userBase.getEmail());
        }

        User user1 = userRepository.save(userBase);
        UserDto userDto = userMapper.toUserDto(user1);
        return userDto;
    }

    @Override
    public List<UserDto> getFindAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserId(Long id) {
        if (id <= 0) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        User userBase = userRepository.getById(id);
        return userMapper.toUserDto(userBase);
    }

    @Override
    public boolean deleteUser(Long id) {
        if (id <= 0) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        User userBase = userRepository.getById(id);
        if (userBase.getId() != null) {
            userRepository.delete(userBase);
        } else {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        return true;
    }

    private User standardCheck(User user) throws ValidationException, ConflictException {
        if (user.getEmail() == null ||
                user.getEmail().isEmpty() ||
                user.getEmail().isBlank() ||
                !user.getEmail().contains("@")) {
            log.error("Неверно введен email: {}", user);
            throw new ValidationException("Неверно введен email");
        }
//        for (User value : users.values()) {
//            if (value.getEmail().equals(user.getEmail())) {
//                log.error("Неверно введен email: {}", user);
//                throw new ConflictException("Неверно введен email");
//            }
//        }
        if (user.getName().isEmpty() || user.getEmail().isBlank() || user.getName().contains(" ")) {
            log.error("Имя пользователя не может быть пустым: {}", user);
            throw new ValidationException("Имя пользователя не может быть пустым");
        }
        return user;
    }
}
