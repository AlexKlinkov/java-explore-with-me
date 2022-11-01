package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.CommentDTOInputForEdit;
import ru.practicum.explore_with_me.dto.CommentDtoOutput;
import ru.practicum.explore_with_me.dto.NewCommentDTOInput;
import ru.practicum.explore_with_me.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    @Autowired
    @Qualifier("CommentServiceInBD")
    private final CommentService commentService;

    @PostMapping("/events/{eventId}/comments")
    public CommentDtoOutput addCommentToEvent (@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                               @PathVariable Long eventId,
                                               @RequestBody NewCommentDTOInput newCommentDTOInput) {
        return commentService.addCommentToEvent(userId, eventId, newCommentDTOInput);
    }

    @DeleteMapping("/events/comments/{comId}")
    public void deleteComment (@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                      @PathVariable Long comId) {
        commentService.deleteComment(userId, comId);
    }

    @PutMapping("/events/comments/{comId}")
    public CommentDtoOutput updateComment (@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                                  @PathVariable Long comId,
                                                  @RequestBody CommentDTOInputForEdit commentDTOInputForEdit) {
        return commentService.updateComment(userId, comId, commentDTOInputForEdit);
    }

    @GetMapping("/events/{eventId}/comments")
    public List<CommentDtoOutput> getCommentsAboutEventById(@PathVariable Long eventId) {
        return commentService.getCommentsAboutEventById(eventId);
    }
}
