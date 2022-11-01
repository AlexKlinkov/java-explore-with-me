package ru.practicum.explore_with_me.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explore_with_me.dto.CommentDtoOutput;
import ru.practicum.explore_with_me.model.Comment;

@Mapper
public interface CommentMapper {
    CommentDtoOutput commentDtoOutputFromComment(Comment comment);
}
