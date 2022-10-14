package ru.practicum.explore_with_me.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.auxiliary_objects.CompilationCheckValidationMethods;
import ru.practicum.explore_with_me.dto.CommentDTOInputForEdit;
import ru.practicum.explore_with_me.dto.CommentDtoOutput;
import ru.practicum.explore_with_me.dto.NewCommentDTOInput;
import ru.practicum.explore_with_me.exeption.ConflictException;
import ru.practicum.explore_with_me.exeption.NotFoundException;
import ru.practicum.explore_with_me.exeption.ServerError;
import ru.practicum.explore_with_me.exeption.ValidationException;
import ru.practicum.explore_with_me.mapper.CommentMapper;
import ru.practicum.explore_with_me.model.Comment;
import ru.practicum.explore_with_me.repository.CommentRepository;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Component("CommentServiceInBD")
@RequiredArgsConstructor
public class CommentServiceInBD implements CommentService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final EventRepository eventRepository;
    @Autowired
    private final CommentRepository commentRepository;
    @Autowired
    @Qualifier("CommentMapperImplForBD")
    final CommentMapper commentMapper;

    @Override
    public CommentDtoOutput addCommentToEvent(Long userId, Long eventId, NewCommentDTOInput newCommentDTOInput) {
        log.debug("Add comment to event by path : '/events/{eventId}/comments'");
        LocalDateTime now = LocalDateTime.now();
        if (CompilationCheckValidationMethods.checkParamOfId(userId)) {
            throw new ValidationException("Id of user cannot be less than 0");
        }
        if (CompilationCheckValidationMethods.checkParamOfId(eventId)) {
            throw new ValidationException("Id of event cannot be less than 0");
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found.");
        }
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found.");
        }
        try {
            // 1. Проверяем, что комментарий хочет оставить не инициатор события
            if (eventRepository.getReferenceById(eventId).getInitiator().getId() != userId) {
                Comment comment = new Comment();
                comment.setComment(newCommentDTOInput.getCommentText());
                comment.setEvent(eventRepository.getReferenceById(eventId));
                comment.setAuthor(userRepository.getReferenceById(userId));
                comment.setCreated(now);
                return commentMapper.commentDtoOutputFromComment(commentRepository.save(comment));
            } else {
                throw new ConflictException("User with id=" + userId + " is initiator of this event, so he cannot " +
                        "add comment to own event");
            }
        }  catch (ServerError exception) {
            throw new ServerError("Error occurred");
        }
    }

    @Override
    public void deleteComment(Long userId, Long comId) {
        log.debug("Delete comment from event by path : '/events/comments/{comId}'");
        if (CompilationCheckValidationMethods.checkParamOfId(userId)) {
            throw new ValidationException("Id of user cannot be less than 0");
        }
        if (CompilationCheckValidationMethods.checkParamOfId(comId)) {
            throw new ValidationException("Id of comment cannot be less than 0");
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found.");
        }
        if (!commentRepository.existsById(comId)) {
            throw new NotFoundException("Comment with id=" + comId + " was not found.");
        }
        try {
            // 1. Проверяем, что комментарий хочет удалить пользователь, который его оставил
            if (commentRepository.getReferenceById(comId).getAuthor().getId() == userId) {
                commentRepository.deleteById(comId);
            } else {
                throw new ConflictException("User with id=" + userId + " cannot delete this comment so " +
                        "this comment is not belonged him");
            }
        } catch (ServerError exception) {
            throw new ServerError("Error occurred");
        }
    }

    @Override
    public CommentDtoOutput updateComment(Long userId, Long comId,
                                          CommentDTOInputForEdit commentDTOInputForEdit) {
        log.debug("User with id=" + userId + " wants to update comment with id=" + comId
                + " by path : '/events/comments/{comId}'");
        if (CompilationCheckValidationMethods.checkParamOfId(userId) ||
                CompilationCheckValidationMethods.checkParamOfId(comId)) {
            throw new ValidationException("Id of user and id of comment cannot be less than 0");
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found.");
        }
        if (!commentRepository.existsById(comId)) {
            throw new NotFoundException("Comment with id=" + comId + " was not found.");
        }
        try {
            // 1. Проверяем, что комментарий хочет отредактировать пользователь, который его оставил
            if (commentRepository.getReferenceById(comId).getAuthor().getId() == userId) {
                // 2. Получаем комментарий, который нужно отредактировать
                Comment comment = commentRepository.getReferenceById(comId);
                comment.setComment(commentDTOInputForEdit.getNewText());
                return commentMapper.commentDtoOutputFromComment(commentRepository.save(comment));
            } else {
                throw new ConflictException("User with id=" + userId + " cannot update this comment so " +
                        "this comment is not belonged him");
            }
        } catch (ServerError exception) {
            throw new ServerError("Error occurred");
        }
    }

    @Override
    public List<CommentDtoOutput> getCommentsAboutEventById(Long eventId) {
        log.debug("Get list with comments to event with id=" + eventId + " by path : '/events/{eventId}/comments'");
        if (CompilationCheckValidationMethods.checkParamOfId(eventId)) {
            throw new ValidationException("Id of event cannot be less than 0");
        }
        if (!userRepository.existsById(eventId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found.");
        }
        try {
            return commentRepository.getAllByEventId(eventId).stream()
                    .map(commentMapper::commentDtoOutputFromComment)
                    .collect(Collectors.toList());
        } catch (ServerError exception) {
            throw new ServerError("Error occurred");
        }
    }
}