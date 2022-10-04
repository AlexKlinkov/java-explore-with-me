package ru.practicum.exploreWithMe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.exploreWithMe.auxiliaryObjects.StatusOfParticipationRequest;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "participation_requests")
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id of request on taking part in event
    @ManyToOne
    @JoinColumn(name = "eventId")
    private Event event;
    @Column(name = "created")
    private LocalDateTime created; // date when this request was created
    @ManyToOne
    @JoinColumn(name = "requestorId")
    private User requestor;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusOfParticipationRequest status; // status approving of request by initiator of this event
}
