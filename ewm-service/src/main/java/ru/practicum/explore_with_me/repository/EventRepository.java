package ru.practicum.explore_with_me.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore_with_me.auxiliary_objects.StatusOfEvent;
import ru.practicum.explore_with_me.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> getAllByStateIs(StatusOfEvent status); // 1
    Event getByIdAndStateIs (Long id, StatusOfEvent state);

    Event getByIdAndStateNot(Long id, StatusOfEvent state);

    List<Event> getAllByInitiatorId(Long initiatorId);
    Event getByInitiatorId(Long initiatorId);

    Event getByIdAndInitiatorId(Long eventId, Long initiatorId);
    Event getByIdAndStateIsAndEventDateIsAfter (Long eventId, StatusOfEvent status, LocalDateTime localDateTime);

}
