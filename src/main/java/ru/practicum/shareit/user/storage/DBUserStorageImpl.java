package ru.practicum.shareit.user.storage;

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

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Primary
public class DBUserStorageImpl implements UserStorage {

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

        User userBase = userRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("Пользователь не найден"));

        if (user.getEmail() != null) {
            if (userRepository.findByEmailNotSelf(user.getEmail(), userBase.getId()) != null) {
                throw new ObjectNotFoundException(String.format("Такой  email s% уже существует", user.getEmail()));
            }
        }
        if (user.getEmail() == null) {
            user.setEmail(userBase.getEmail());
        } else if (user.getName() == null) {
            user.setName(userBase.getName());
        }
        user.setId(id);
        User user1 = userRepository.save(user);
        return userMapper.toUserDto(user1);
    }

    @Override
    public List<UserDto> getFindAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserDtoById(Long id) {
        if (id <= 0) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }

        User user= userRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("Пользователь не найден"));

        return userMapper.toUserDto(user);
    }

    @Override
    public User getUserById(Long id) {
        if (id <= 0) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }

        User user= userRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("Пользователь не найден"));

        return user;
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

        if (user.getName().isEmpty() || user.getEmail().isBlank() || user.getName().contains(" ")) {
            log.error("Имя пользователя не может быть пустым: {}", user);
            throw new ValidationException("Имя пользователя не может быть пустым");
        }
        return user;
    }
}
