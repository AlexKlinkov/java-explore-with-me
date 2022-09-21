package ru.practicum.exploreWithMe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.exploreWithMe.auxiliaryObjects.StatusOfParticipationRequest;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequest {
    private Long id; // id of request on taking part in event
    private Long eventId; // id of this event
    private LocalDateTime created; // date when this request was created
    private Long requestorId; // UserID which apply claim on taking part in this event
    private StatusOfParticipationRequest status; // status approving of request by initiator of this event
}
