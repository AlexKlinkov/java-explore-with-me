package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.dto.CommentDTOInputForEdit;
import ru.practicum.exploreWithMe.dto.CommentDtoOutput;
import ru.practicum.exploreWithMe.dto.NewCommentDTOInput;
import ru.practicum.exploreWithMe.mapper.CommentMapper;
import ru.practicum.exploreWithMe.model.Comment;
import ru.practicum.exploreWithMe.repository.CommentRepository;
import ru.practicum.exploreWithMe.repository.EventRepository;
import ru.practicum.exploreWithMe.repository.UserRepository;

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
        // 1. Проверяем, что комментарий хочет оставить не инициатор события
        if (eventRepository.getReferenceById(eventId).getInitiator().getId() != userId) {
            Comment comment = new Comment();
            comment.setComment(newCommentDTOInput.getCommentText());
            comment.setEvent(eventRepository.getReferenceById(eventId));
            comment.setAuthor(userRepository.getReferenceById(userId));
            comment.setCreated(LocalDateTime.now());
            return commentMapper.commentDtoOutputFromComment(commentRepository.save(comment));
        }
        return null;
    }

    @Override
    public void deleteComment(Long userId, Long comId) {
        log.debug("Delete comment from event by path : '/events/comments/{comId}'");
        // 1. Проверяем, что комментарий хочет удалить пользователь, который его оставил
        if (commentRepository.getReferenceById(comId).getAuthor().getId() == userId) {
            commentRepository.deleteById(comId);
        }
    }

    @Override
    public CommentDtoOutput updateComment(Long userId, Long comId,
                                                 CommentDTOInputForEdit commentDTOInputForEdit) {
        log.debug("Delete comment from event by path : '/events/comments/{comId}'");
        // 1. Проверяем, что комментарий хочет отредактировать пользователь, который его оставил
        if (commentRepository.getReferenceById(comId).getAuthor().getId() == userId) {
            // 2. Получаем комментарий, который нужно отредактировать
            Comment comment = commentRepository.getReferenceById(comId);
            comment.setComment(commentDTOInputForEdit.getNewText());
            return commentMapper.commentDtoOutputFromComment(commentRepository.save(comment));
        }
        return null;
    }

    @Override
    public List<CommentDtoOutput> getCommentsAboutEventById(Long eventId) {
        return commentRepository.getAllByEventId(eventId).stream()
                .map(commentMapper::commentDtoOutputFromComment)
                .collect(Collectors.toList());
    }
}
