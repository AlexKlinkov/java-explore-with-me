package ru.practicum.exploreWithMe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.dto.CommentDTOInputForEdit;
import ru.practicum.exploreWithMe.dto.CommentDtoOutput;
import ru.practicum.exploreWithMe.dto.NewCommentDTOInput;
import ru.practicum.exploreWithMe.service.CommentService;

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

    @PatchMapping("/events/comments/{comId}")
    public CommentDtoOutput updateCommentt (@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                                  @PathVariable Long comId,
                                                  @RequestBody CommentDTOInputForEdit commentDTOInputForEdit) {
        return commentService.updateComment(userId, comId, commentDTOInputForEdit);
    }

    @GetMapping("/events/{eventId}/comments")
    public List<CommentDtoOutput> getCommentsAboutEventById(@PathVariable Long eventId) {
        return commentService.getCommentsAboutEventById(eventId);
    }
}
