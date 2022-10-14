package ru.practicum.explore_with_me.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore_with_me.auxiliary_objects.StatusOfParticipationRequest;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDtoOutput {
    private Long id; // id of request on taking part in event
    @JsonProperty(value = "event")
    private Long eventId; // id of this event
    private LocalDateTime created; // date when this request was created
    @JsonProperty(value = "requester")
    private Long requesterId; // UserID which apply claim on taking part in this event
    private StatusOfParticipationRequest status; // status approving of request by initiator of this event
}
