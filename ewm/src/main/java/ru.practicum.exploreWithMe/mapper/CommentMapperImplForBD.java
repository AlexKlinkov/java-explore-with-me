package ru.practicum.exploreWithMe.mapper;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.dto.CommentDtoOutput;
import ru.practicum.exploreWithMe.dto.NewCommentDTOInput;
import ru.practicum.exploreWithMe.model.Comment;
import ru.practicum.exploreWithMe.repository.EventRepository;
import ru.practicum.exploreWithMe.repository.UserRepository;

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
