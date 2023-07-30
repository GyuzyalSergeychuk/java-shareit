package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findById(Long id);
    List<Item> findByOwnerId(Long id);

    @Query("SELECT i From Item i " +
            "WHERE i.id = :id and i.ownerId = :ownerId")
    Item findByIdSelf(@Param("id") Long id,
                      @Param("ownerId") Long ownerId);

    List<Item> findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(String name, String description);

}
