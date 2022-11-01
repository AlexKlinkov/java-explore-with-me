package ru.practicum.explore_with_me.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCommentDTOInput {
    @Positive
    private Long eventId;
    @Size(min = 20, max = 1000)
    private String commentText;
}
