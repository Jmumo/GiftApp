package GiftsBackend.Repository;

import GiftsBackend.Model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {
    List<Event> findByUserId(Long id);

    Event findByPaymentRef(String paymentref);


    List<Event> findByStartDateAndEndDate(LocalDate startDate, LocalDate endDate);


}
