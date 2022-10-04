package ru.practicum.exploreWithMe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.exploreWithMe.model.ParticipationRequest;

import javax.transaction.Transactional;
import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> getAllByRequestorId(Long requestorId);
    @Transactional
    @Modifying
    @Query("update ParticipationRequest p set p.status = :status where p.requestor.id = :requestorId " +
            "and p.id = :requestId")
    ParticipationRequest cancelOwnRequest (@Param("requestorId") Long requestorId,
                                 @Param("requestId") Long requestId,
                                 @Param("status") String status);
}
