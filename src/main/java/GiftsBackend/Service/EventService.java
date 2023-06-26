package GiftsBackend.Service;

import GiftsBackend.Dtos.EventDto;
import GiftsBackend.Dtos.ImageResponseDto;
import GiftsBackend.Dtos.ProductEventDto;
import GiftsBackend.Dtos.SaveUSerEventDto;
import GiftsBackend.Model.Event;
import GiftsBackend.Model.EventCategory;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface EventService {

    List<Event> getUserEvents(String email);

    EventDto addEvent(EventDto eventDto);

    EventCategory saveEventCategory(MultipartFile image,String name);

    Event addproductToEvent(ProductEventDto productEventDto);

    ImageResponseDto UpdateProfileImage(MultipartFile image);

    Event addproductToEvent(Long eventId, Long productId);
}
