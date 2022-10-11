package ru.practicum.exploreWithMe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.exploreWithMe.auxiliaryClasses.Attributes;
import ru.practicum.exploreWithMe.model.EndpointHit;

import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {

    List<EndpointHit> findAllByAppIs(String app);
}
