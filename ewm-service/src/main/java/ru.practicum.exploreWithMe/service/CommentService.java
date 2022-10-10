package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.dto.CommentDTOInputForEdit;
import ru.practicum.exploreWithMe.dto.CommentDtoOutput;
import ru.practicum.exploreWithMe.dto.NewCommentDTOInput;

import java.util.List;

public interface CommentService {
    // comment cannot be rest by initiator of this event
    // POST /events/{eventId}/comments
    CommentDtoOutput addCommentToEvent ( Long userId, Long eventId, NewCommentDTOInput newCommentDTOInput);
    // comment can be deleted only by person who remain this comment
    // DELETE /events/comments/{comId}
    void deleteComment (Long userId, Long comId);
    // comment can be edited only by person who remain this comment
    // PATCH /events/comments/{comId}
    CommentDtoOutput updateComment(Long userId, Long comId, CommentDTOInputForEdit commentDTOInputForEdit);
    // Return list with all comments to this event
    // GET /events/{eventId}/comments
    List<CommentDtoOutput> getCommentsAboutEventById(Long eventId);
}
