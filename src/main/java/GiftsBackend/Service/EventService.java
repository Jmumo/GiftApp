package GiftsBackend.Service;

import GiftsBackend.Dtos.*;
import GiftsBackend.Model.Event;
import GiftsBackend.Model.EventCategory;
import GiftsBackend.Model.Product;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface EventService {

    List<Event> getUserEvents();

    Event addEvent(EventDto eventDto);

    EventCategory saveEventCategory(MultipartFile image,String name);

    Event addproductToEvent(ProductEventDto productEventDto);

    ImageResponseDto UpdateProfileImage(MultipartFile image);

    Event addproductToEvent(Long eventId, Long productId);

    Event getEvent(Long id);

    Event confirm(ConfirmEventDto confirmEventDto);

    Event contributeToEvent(ContributeEventDto contributeEventDto);

    List<Event> getCalendarEvents(LocalDate startDate, LocalDate endDate);

    List<Event> searchProduct(String name, String sort, Integer pageNumber, Integer pageSize, Sort.Direction sortdirection);
}
