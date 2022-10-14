package ru.practicum.explore_with_me.mapper;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.dto.CommentDtoOutput;
import ru.practicum.explore_with_me.model.Comment;

@Data
@Slf4j
@Component("CommentMapperImplForBD")
@RequiredArgsConstructor
public class CommentMapperImplForBD implements CommentMapper {

    @Override
    public CommentDtoOutput commentDtoOutputFromComment(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentDtoOutput commentDtoOutput = new CommentDtoOutput();
        commentDtoOutput.setId(comment.getId());
        commentDtoOutput.setAuthorName(comment.getAuthor().getName());
        commentDtoOutput.setEventId(comment.getEvent().getId());
        commentDtoOutput.setCommentText(comment.getComment());
        commentDtoOutput.setCreated(comment.getCreated());
        return commentDtoOutput;
    }
}
