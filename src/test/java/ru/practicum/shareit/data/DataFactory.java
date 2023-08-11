package ru.practicum.shareit.data;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForGetItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
public class DataFactory {

    public static User getUser(Long id, String name, String email) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
//        user.setItems(items);
        return user;
    }

    public static UserDto getUserDto(Long id, String name, String email) {
        return UserDto.builder()
                .id(id)
                .name(name)
                .email(email)
                .build();
    }

    public static Item getItem(Long id, String name, String description, Boolean available, Long ownerId) {
        //Long requestId, Long lastBookingId, Long nextBookingId, List<Comment> comments) {
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setDescription(description);
        item.setAvailable(available);
        item.setOwnerId(ownerId);
//        item.setRequestId(requestId);
//        item.setLastBookingId(lastBookingId);
//        item.setNextBookingId(nextBookingId);
//        item.setComments(comments);
        return item;
    }

    public static ItemDto getItemDto(Long id, String name, String description, Boolean available, Long ownerId) {
//                                     BookingForGetItemDto lastBookingId, BookingForGetItemDto nextBookingId,
//                                     List<CommentDto> comments, Long requestId) {
        return ItemDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .available(available)
                .ownerId(ownerId)
//                .lastBooking(lastBookingId)
//                .nextBooking(nextBookingId)
//                .comments(comments)
//                .requestId(requestId)
                .build();
    }

//    public static ItemDto getItemDtoBooking(Long id, String name, String description, Boolean available, Long ownerId,
//                                     Long lastBookingId, Long nextBookingId) {
////                                     List<CommentDto> comments, Long requestId) {
//        return ItemDto.builder()
//                .id(id)
//                .name(name)
//                .description(description)
//                .available(available)
//                .ownerId(ownerId)
//                .lastBooking(lastBookingId)
//                .nextBooking(nextBookingId)
////                .comments(comments)
////                .requestId(requestId)
//                .build();
//    }

    public static Booking getBooking(Long id, LocalDateTime start, LocalDateTime end, Long itemId, Long bookerId,
                                     Status status, Long itemOwnerId) {
        Booking booking = new Booking();
        booking.setId(id);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setItemId(itemId);
        booking.setBookerId(bookerId);
        booking.setStatus(status);
        booking.setItemOwnerId(itemOwnerId);
        return booking;
    }

    public static BookingDto getBookingDto(Long id, LocalDateTime start, LocalDateTime end, ItemDto item,
                                           UserDto booker, Status status, Long itemOwnerId) {
        return BookingDto.builder()
                .id(id)
                .start(start)
                .end(end)
                .item(item)
                .booker(booker)
                .status(status)
                .itemOwner(itemOwnerId)
                .build();
    }

    public static Comment getComment(Long id, String text, Long authorId, LocalDateTime created, Long itemId) {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setText(text);
        comment.setAuthorId(authorId);
        comment.setCreated(created);
        comment.setItemId(itemId);
        return comment;
    }

    public static CommentDto getCommentDto(Long id, String text, String authorName, LocalDateTime created){
        return CommentDto.builder()
                .id(id)
                .text(text)
                .authorName(authorName)
                .created(created)
                .build();
    }

    public static ItemRequest getItemRequest(Long id, String description, LocalDateTime created, Long userId,
                                             List<Item> items){
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(id);
        itemRequest.setDescription(description);
        itemRequest.setCreated(created);
        itemRequest.setUserId(userId);
        itemRequest.setItems(items);
        return itemRequest;
    }

    public static ItemRequestDto getItemRequestDto(Long id, String description, LocalDateTime created, Long userId,
                                                   List<ItemDto> items){
        return ItemRequestDto.builder()
                .id(id)
                .description(description)
                .created(created)
                .userId(userId)
                .items(items)
                .build();
    }
}
