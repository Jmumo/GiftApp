package GiftsBackend.Service.impl;

import GiftsBackend.Dtos.SaveUSerEventDto;
import GiftsBackend.Model.Event;
import GiftsBackend.Model.User;
import GiftsBackend.Repository.EventRepository;
import GiftsBackend.Repository.UserRepository;
import GiftsBackend.Service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public Event addEvent(SaveUSerEventDto event) {

        Optional<User> user = userRepository.findById(event.getUserId());

        var eventToSave = Event.builder()
                .name(event.getName())
                .productId(null)
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .user(user.get())
                .build();


        return eventRepository.save(eventToSave);
    }

    @Override
    public List<Event> getUserEvents(String email) {

        Optional<User> user = userRepository.findByEmail(email);

        List<Event> userEvents = eventRepository.findByUserId(user.get().getId());

        return userEvents;
    }
}
