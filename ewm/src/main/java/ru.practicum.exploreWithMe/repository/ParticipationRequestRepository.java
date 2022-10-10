package ru.practicum.exploreWithMe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.exploreWithMe.auxiliaryObjects.StatusOfParticipationRequest;
import ru.practicum.exploreWithMe.model.ParticipationRequest;

import javax.transaction.Transactional;
import java.util.List;
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> getAllByRequestorId(Long requestorId);
    ParticipationRequest getByIdAndRequestorId(Long requestId, Long userId);
    List<ParticipationRequest> getAllByEventIdAndStatusIs(Long eventId, StatusOfParticipationRequest status);
    ParticipationRequest findByRequestorIdAndEventId (Long requestorId, Long eventId);
}
