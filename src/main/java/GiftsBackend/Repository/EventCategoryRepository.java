package GiftsBackend.Repository;

import GiftsBackend.Model.EventCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventCategoryRepository extends JpaRepository<EventCategory,Long> {
    Optional<EventCategory> findByName(String name);
}
