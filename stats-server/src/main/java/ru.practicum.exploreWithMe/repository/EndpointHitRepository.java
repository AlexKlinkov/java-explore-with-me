package ru.practicum.exploreWithMe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.exploreWithMe.model.EndpointHit;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {
}
