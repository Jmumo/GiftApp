package GiftsBackend.Repository;

import GiftsBackend.Model.EventType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvetypeRepository extends JpaRepository<EventType, Long> {
}
