package ru.practicum.exploreWithMe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDtoOutput {
    @PositiveOrZero
    private Long eventId; // id of this event
    @PositiveOrZero
    private Long requestorId; // UserID which apply claim on taking part in this event
}
