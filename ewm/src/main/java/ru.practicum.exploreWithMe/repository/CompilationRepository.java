package ru.practicum.exploreWithMe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.exploreWithMe.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
}
