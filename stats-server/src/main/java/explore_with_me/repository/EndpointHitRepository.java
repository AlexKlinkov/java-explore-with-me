package explore_with_me.repository;

import explore_with_me.model.EndpointHit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {

    List<EndpointHit> findAllByAppIs(String app);
}
