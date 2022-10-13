package ru.practicum.exploreWithMe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.exploreWithMe.model.EndpointHit;

import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {

    List<EndpointHit> findAllByAppIs(String app);
}
