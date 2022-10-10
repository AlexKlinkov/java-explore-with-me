package ru.practicum.exploreWithMe.mapper;

import org.mapstruct.Mapper;
import ru.practicum.exploreWithMe.dto.CommentDtoOutput;
import ru.practicum.exploreWithMe.model.Comment;

@Mapper
public interface CommentMapper {
    CommentDtoOutput commentDtoOutputFromComment(Comment comment);
}
