package ru.practicum.exploreWithMe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.exploreWithMe.model.CompilationEvents;

public interface CompilationEventsRepository extends JpaRepository<CompilationEvents, Long> {
}
