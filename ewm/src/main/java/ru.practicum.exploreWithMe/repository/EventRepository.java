package ru.practicum.exploreWithMe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.exploreWithMe.auxiliaryObjects.StatusOfEvent;
import ru.practicum.exploreWithMe.model.Event;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> getAllByStateIs(StatusOfEvent status); // 1
    Event getByIdAndStateIs (Long id, StatusOfEvent state);

    Event getByIdAndStateNot(Long id, StatusOfEvent state);

    List<Event> getAllByInitiatorId(Long initiatorId);
    Event getByInitiatorId(Long initiatorId);

    Event getByIdAndInitiatorId(Long eventId, Long initiatorId);
    /*@Transactional*/
    Event getByIdAndStateIsAndEventDateIsAfter (Long eventId, StatusOfEvent status, LocalDateTime localDateTime);

}
