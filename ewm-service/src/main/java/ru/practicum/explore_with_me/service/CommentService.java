package ru.practicum.explore_with_me.service;

import ru.practicum.explore_with_me.dto.CommentDTOInputForEdit;
import ru.practicum.explore_with_me.dto.CommentDtoOutput;
import ru.practicum.explore_with_me.dto.NewCommentDTOInput;

import java.util.List;

public interface CommentService {
    /** comment cannot be rest by initiator of this event
        * POST /events/{eventId}/comments
    */
    CommentDtoOutput addCommentToEvent (Long userId, Long eventId, NewCommentDTOInput newCommentDTOInput);
    /** comment can be deleted only by person who remain this comment
        * DELETE /events/comments/{comId}
    */
    void deleteComment (Long userId, Long comId);
    /** comment can be edited only by person who remain this comment
        * PUT /events/comments/{comId}
    */
    CommentDtoOutput updateComment(Long userId, Long comId, CommentDTOInputForEdit commentDTOInputForEdit);
    /** Return list with all comments to this event
        * GET /events/{eventId}/comments
    */
    List<CommentDtoOutput> getCommentsAboutEventById(Long eventId);
}
