package ru.practicum.explore_with_me.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import ru.practicum.explore_with_me.model.CompilationEvents;

import javax.transaction.Transactional;
import java.util.List;

public interface CompilationEventsRepository extends JpaRepository<CompilationEvents, Long> {
    List<CompilationEvents> getAllByCompilationId(Long compilationId);
    @Transactional
    @Modifying
    void deleteAllByCompilationId(Long compId);

    @Transactional
    @Modifying
    void deleteAllByCompilationIdAndEventId(Long compId, Long eventId);
    CompilationEvents getByCompilationIdAndAndEventId(Long compId, Long eventId);
}
