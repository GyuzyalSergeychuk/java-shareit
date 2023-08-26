package ru.practicum.shareit.user.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class DBUserStorageImpl implements UserStorage {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto create(User user) throws ValidationException, ConflictException {
        User user1 = userRepository.save(user);
        UserDto userDto = userMapper.toUserDto(user1);
        log.info("Пользователь успешно добавлен {}", user.getId());
        return userDto;
    }

    @Override
    public UserDto update(Long id, User user) throws ValidationException, ConflictException {
        User userBase = userRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("Пользователь не найден"));

        if (user.getEmail() != null && userRepository.findByEmailNotSelf(user.getEmail(), userBase.getId()) != null) {
            throw new ConflictException(String.format("Такой  email %s уже существует", user.getEmail()));
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
        return userMapper.toUserDto(userRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("Пользователь не найден")));
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("Пользователь не найден"));
    }

    @Override
    public boolean deleteUser(Long id) {
        User userBase = userRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("Пользователь не найден"));
        userRepository.delete(userBase);
        return true;
    }
}
