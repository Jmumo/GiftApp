package GiftsBackend.Service.impl;

import GiftsBackend.Dtos.ProductEventDto;
import GiftsBackend.Dtos.SaveUSerEventDto;
import GiftsBackend.Model.Event;
import GiftsBackend.Model.EventCategory;
import GiftsBackend.Model.Product;
import GiftsBackend.Model.User;
import GiftsBackend.Repository.EventCategoryRepository;
import GiftsBackend.Repository.EventRepository;
import GiftsBackend.Repository.ProductRepository;
import GiftsBackend.Repository.UserRepository;
import GiftsBackend.Service.EventService;
import com.cloudinary.Cloudinary;
import jdk.jfr.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;


@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final Cloudinary cloudinary;
    private final ProductRepository productRepository;


    @Override
    public List<Event> getUserEvents(String email) {

        Optional<User> user = userRepository.findByEmail(email);

        List<Event> userEvents = eventRepository.findByUserId(user.get().getId());

        return userEvents;
    }

    @Override
    public Event addEvent(MultipartFile image, String name, LocalDate startDate, LocalDate endDate, String location, String category, String details, Long productId, String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);

        Optional<EventCategory> eventcategory = eventCategoryRepository.findByName(category);

        String imageUrl;
        try {
            imageUrl = cloudinary.uploader()
                    .upload(image.getBytes(),
                            Map.of("public_id", UUID.randomUUID().toString())).get("url").toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        var eventToSave = Event.builder()
                .name(name)
                .product(new ArrayList<>())
                .startDate(startDate)
                .imageUrl(imageUrl)
                .endDate(endDate)
                .user(user.get())
                .details(details)
                .Location(location)
                .eventCategory(eventcategory.get())
                .build();


        return eventRepository.save(eventToSave);


    }

    @Override
    public EventCategory saveEventCategory(String name) {
      EventCategory eventCategory = new EventCategory();
      eventCategory.setName(name);

        return eventCategoryRepository.save(eventCategory);
    }

    @Override
    public Event addproductToEvent(ProductEventDto productEventDto) {
        Optional<Product> product = productRepository.findById(productEventDto.getProductId());

        Optional<Event> event = eventRepository.findById(productEventDto.getEventId());

        if (product.isPresent() && event.isPresent()) {
            Product UpdatedProduct = product.get();

            Event UpdatedEvent = event.get();

            UpdatedEvent.getProduct().add(UpdatedProduct);

            return eventRepository.save(UpdatedEvent);
        }
        return null;

    }
}
