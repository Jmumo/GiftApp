package GiftsBackend.Repository;

import GiftsBackend.Model.Event;
import GiftsBackend.Model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {
    List<Event> findByUserId(Long id);

    Event findByPaymentRef(String paymentref);


    List<Event> findByStartDateAndEndDate(LocalDate startDate, LocalDate endDate);


    List<Event> findAll(Specification<Event> cardSpecification, Pageable pageable);


}
