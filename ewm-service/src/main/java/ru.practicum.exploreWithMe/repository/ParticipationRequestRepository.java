package ru.practicum.exploreWithMe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.exploreWithMe.auxiliary_objects.StatusOfParticipationRequest;
import ru.practicum.exploreWithMe.model.ParticipationRequest;

import java.util.List;
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> getAllByRequestorId(Long requestorId);
    List<ParticipationRequest> getAllByEventIdAndStatusIs(Long eventId, StatusOfParticipationRequest status);
    ParticipationRequest findByRequestorIdAndEventId (Long requestorId, Long eventId);
}
