package ru.practicum.exploreWithMe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.exploreWithMe.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
