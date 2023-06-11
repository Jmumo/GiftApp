package GiftsBackend.Service;

import GiftsBackend.Dtos.ProductEventDto;
import GiftsBackend.Dtos.SaveUSerEventDto;
import GiftsBackend.Model.Event;
import GiftsBackend.Model.EventCategory;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface EventService {

    public List<Event> getUserEvents(String email);

    Event addEvent(MultipartFile image, String name, LocalDate startDate, LocalDate endDate, String location, String category, String details, Long productId, String userEmail);

    EventCategory saveEventCategory(MultipartFile image,String name);

    Event addproductToEvent(ProductEventDto productEventDto);
}
