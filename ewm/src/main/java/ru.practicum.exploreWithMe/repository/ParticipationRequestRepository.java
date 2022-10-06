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
    @Transactional
    @Modifying
    @Query("update ParticipationRequest p set p.status = :status where p.requestor.id = :requestorId " +
            "and p.id = :requestId")
    void cancelOwnRequest (@Param("requestorId") Long requestorId,
                                 @Param("requestId") Long requestId,
                                 @Param("status") StatusOfParticipationRequest status);

    List<ParticipationRequest> getAllByEventId(Long eventId);
}
