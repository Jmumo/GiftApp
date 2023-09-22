package GiftsBackend.Service;

import GiftsBackend.Dtos.*;
import GiftsBackend.Model.Event;
import GiftsBackend.Model.EventCategory;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface EventService {

    List<Event> getUserEvents(String email);

    Event addEvent(EventDto eventDto);

    EventCategory saveEventCategory(MultipartFile image,String name);

    Event addproductToEvent(ProductEventDto productEventDto);

    ImageResponseDto UpdateProfileImage(MultipartFile image);

    Event addproductToEvent(Long eventId, Long productId);

    Event getEvent(Long id);

    Event confirm(ConfirmEventDto confirmEventDto);

    Event contributeToEvent(ContributeEventDto contributeEventDto);

    List<Event> getCalendarEvents(LocalDate startDate, LocalDate endDate);
}
