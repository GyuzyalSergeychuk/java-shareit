package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);
    @Query("SELECT u From User u " +
            "WHERE u.email = :email and u.id is not :id")
    User findByEmailNotSelf(@Param("email") String email,
                            @Param("id") Long id);
}