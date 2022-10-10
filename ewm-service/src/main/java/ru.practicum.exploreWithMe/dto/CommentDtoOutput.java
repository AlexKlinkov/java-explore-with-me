package ru.practicum.exploreWithMe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDtoOutput {
    private Long id;
    private Long eventId;
    private String authorName;
    private String commentText;
    private LocalDateTime created;
}
