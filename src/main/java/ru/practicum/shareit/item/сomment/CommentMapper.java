package ru.practicum.shareit.item.сomment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CommentMapper {

    private final UserRepository userRepository;

    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(userRepository.findById(comment.getAuthorId()).get().getName())
                .created(comment.getCreated())
                .build();
    }
}
