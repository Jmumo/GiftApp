package GiftsBackend.Service.impl;

import GiftsBackend.Dtos.*;
import GiftsBackend.Execptions.InvalidTokenException;
import GiftsBackend.Execptions.UserNotFoundException;
import GiftsBackend.Model.*;
import GiftsBackend.Repository.EventCategoryRepository;
import GiftsBackend.Repository.EventRepository;
import GiftsBackend.Repository.ProductRepository;
import GiftsBackend.Repository.UserRepository;
import GiftsBackend.Service.EventService;
import GiftsBackend.Utils.HelperUtility;
import GiftsBackend.Utils.SearchSpecifications;
import com.cloudinary.Cloudinary;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.jpa.QueryHints;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final Cloudinary cloudinary;
    private final ProductRepository productRepository;
    private final SearchSpecifications<Event> filterSpecifications;

    private final EntityManager entityManager;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public List<Event> getUserEvents() {

        Optional<User> user = Optional.ofNullable(getCurrentLoggedInUserEmail());
//        System.out.println("getting the user");

        if(user.isPresent()){
            List<Event> userEvents = eventRepository.findByUserId(user.get().getId());
//            System.out.println("getting the user events");

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
                .eventStatus(EventStatus.UNCONFIRMED)
                .contributedAmount(BigDecimal.ZERO)
                .PayBillResponses(new HashSet<>())
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

    @Override
    public Event confirm(ConfirmEventDto confirmEventDto) {

        Event fetchedEvent = eventRepository.findById(confirmEventDto.getEventId()).get();
        log.error("fetched event {}",fetchedEvent.getName());

        Product product = productRepository.findById(confirmEventDto.getProductId()).get();
        log.error("fetched product {}",product);

        User user = userRepository.findByEmail(confirmEventDto.getUserEmail()).get();
        log.error("fetched user {}",user);

        String PayMentRef = HelperUtility.GeneratePaymentRef();


        Set<Product> products = new HashSet<>();
        products.add(product);
        fetchedEvent.setProducts(products);
        fetchedEvent.setCost(product.getPrice() );
        fetchedEvent.setCategory(confirmEventDto.getCategory());
        fetchedEvent.setName(confirmEventDto.getName());
        fetchedEvent.setStartDate(confirmEventDto.getStartDate());
        fetchedEvent.setEndDate(confirmEventDto.getEndDate());
        fetchedEvent.setStartTime(confirmEventDto.getStartTime());
        fetchedEvent.setEndTime(confirmEventDto.getEndTime());
        fetchedEvent.setLocation(confirmEventDto.getLocation());
        fetchedEvent.setDetails(confirmEventDto.getDetails());
        fetchedEvent.setUser(user);
        fetchedEvent.setImageUrl(confirmEventDto.getImageUrl());
        fetchedEvent.setColor(confirmEventDto.getColor());
        fetchedEvent.setEventStatus(EventStatus.CONFIRMED);
        fetchedEvent.setPaymentRef(PayMentRef);
        fetchedEvent.setContributedAmount(BigDecimal.ZERO);

        return eventRepository.save(fetchedEvent);
    }

    @Override
    public Event contributeToEvent(ContributeEventDto contributeEventDto) {
        Event fetchedEvent = eventRepository.findById(contributeEventDto.getEventID()).get();
        log.error("fetched event {}",fetchedEvent.getName());

        return null;
    }

    @Override
    public List<Event> getCalendarEvents(LocalDate startDate, LocalDate endDate) {
        return eventRepository.findByStartDateAndEndDate(startDate,endDate);
    }

    @Override
    public List<Event> searchProduct(String name,  Integer pageNumber, Integer pageSize,String occassion,String color,String dateFilterDirection) {



//        Specification<Event> cardSpecification = filterSpecifications.searchSpecification(name);
//
//        if(pageNumber == null){
//            pageNumber = 0;
//        }
//
//        if(pageSize == null){
//            pageSize =1;
//        }
//        if(sort == null){
//            sort = "startDate";
//        }
//
//        if(sortdirection == null){
//            sortdirection = Sort.Direction.ASC;
//        }else {
//            sortdirection = Sort.Direction.DESC;
//        }
//
//        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortdirection,sort));
//
//        return eventRepository.findAll(cardSpecification,pageable);

  //      Pageable pageable = PageRequest.of(pageNumber,pageSize);

        List<Event> events = searchAndFilterEvents(name, occassion, color, dateFilterDirection,pageNumber,pageSize);
        return events;


    }



    public User getCurrentLoggedInUserEmail(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if(email.isEmpty()){
            throw new InvalidTokenException("No User Authentication found");
        }
       return userRepository.findByEmail(email).get();
    }


   private List<Event> searchAndFilterEvents(
           String name,
           String occasion,
           String color,
           String dateFilterDirection,
           Integer pageNumber,
           Integer pageSize
    ){

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);
        Root<Event> root = criteriaQuery.from(Event.class);

        List<Predicate> predicates = new ArrayList<>();

        if(name != null){
            predicates.add(criteriaBuilder.like(root.get("name"),name));
        }

        if(occasion != null){
            predicates.add(criteriaBuilder.equal(root.get("category"),occasion));
        }

        if(color != null){
            predicates.add(criteriaBuilder.equal(root.get("color"),color));
        }

        if(dateFilterDirection != null){
            if(dateFilterDirection.equals("ASC")) {
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get("startDate")));
            }
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("startDate")));

        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));


        TypedQuery<Event> typedQuery = entityManager.createQuery(criteriaQuery);
        // Enable logging for this query
        typedQuery.setHint(QueryHints.HINT_COMMENT, "Generated SQL: " + criteriaQuery.toString());


        PageRequest pageRequest = PageRequest.of(pageNumber,pageSize);

        // Print the generated SQL query
        System.out.println("Generated SQL Query: " + typedQuery.unwrap(org.hibernate.query.Query.class).getQueryString());

        return entityManager.createQuery(criteriaQuery)
        .setFirstResult((int) pageRequest.getOffset())
        .setMaxResults(pageRequest.getPageSize())
        .getResultList();

    }

}






//List<Product> products = entityManager.createQuery(criteriaQuery)
//        .setFirstResult((int) pageable.getOffset())
//        .setMaxResults(pageable.getPageSize())
//        .getResultList();
//
//// Count the total number of records without pagination
//CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
//        countQuery.select(criteriaBuilder.count(countQuery.from(Product.class)));
//Long totalCount = entityManager.createQuery(countQuery).getSingleResult();
//
//// Return a page containing the results and total count
//        return new PageImpl<>(products, pageable, totalCount);
//        }
