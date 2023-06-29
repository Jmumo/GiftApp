package GiftsBackend.Service.impl;

import GiftsBackend.Dtos.EventDto;
import GiftsBackend.Dtos.ImageResponseDto;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.Category;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.DataOutput;
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
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public List<Event> getUserEvents(String email) {

        Optional<User> user = userRepository.findByEmail(email);

        if(user.isPresent()){
            List<Event> userEvents = eventRepository.findByUserId(user.get().getId());

            if(userEvents.isEmpty()){
                return new ArrayList<>();
            }
            return userEvents;
        }

      return new ArrayList<>();

    }

    @Override
    public Event addEvent(EventDto eventDto) {
        System.out.println("inside image");
        Optional<User> user = userRepository.findByEmail(eventDto.getUserEmail());

        System.out.println("inside image2");


        var eventToSave = Event.builder()
                .name(eventDto.getName())
                .products(new HashSet<>())
                .startDate(eventDto.getStartDate())
                .imageUrl(eventDto.getImageUrl())
                .endDate(eventDto.getEndDate())
                .user(user.get())
                .details(eventDto.getDetails())
                .Location(eventDto.getLocation())
                .category(eventDto.getCategory())
                .startTime(eventDto.getStartTime())
                .endTime(eventDto.getEndTime())
                .color(eventDto.getColor())
                .cost(eventDto.getCost())
                .build();

      return   eventRepository.save(eventToSave);

    }

    @Override
    public EventCategory saveEventCategory(MultipartFile image,String name) {

        String imageUrl;
        try {
            imageUrl = cloudinary.uploader()
                    .upload(image.getBytes(),
                            Map.of("public_id", UUID.randomUUID().toString())).get("url").toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
      EventCategory eventCategory = new EventCategory();
      eventCategory.setName(name);
      eventCategory.setImageUrl(imageUrl);

        return eventCategoryRepository.save(eventCategory);
    }

    @Override
    public Event addproductToEvent(ProductEventDto productEventDto) {
        Optional<Product> product = productRepository.findById(productEventDto.getProductId());

        Optional<Event> event = eventRepository.findById(productEventDto.getEventId());

        if (product.isPresent() && event.isPresent()) {
            Product UpdatedProduct = product.get();

            Event UpdatedEvent = event.get();

//            UpdatedEvent.getProduct().add(UpdatedProduct);

            return eventRepository.save(UpdatedEvent);
        }
        return null;

    }

    @Override
    public ImageResponseDto UpdateProfileImage(MultipartFile image) {

        String imageUrl;
        try {
            imageUrl = cloudinary.uploader()
                    .upload(image.getBytes(),
                            Map.of("public_id", UUID.randomUUID().toString())).get("url").toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ImageResponseDto.builder().url(imageUrl).build();
    }

    @Override
    public Event addproductToEvent(Long eventId, Long productId) {

        Product product = productRepository.findById(productId).get();

        Event event = eventRepository.findById(eventId).get();

        if(product != null && event !=null){
            event.getProducts().add(product);
            event.setCost(event.getCost().add(product.getPrice()));
            return eventRepository.save(event);
        }
        return null;
    }

    @Override
    public Event getEvent(Long id) {
        return eventRepository.findById(id).get();
    }
}
