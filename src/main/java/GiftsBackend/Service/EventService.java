package GiftsBackend.Service;

import GiftsBackend.Dtos.SaveUSerEventDto;
import GiftsBackend.Model.Event;

import java.util.List;

public interface EventService {
    public Event addEvent(SaveUSerEventDto saveUSerEventDto);

    public List<Event> getUserEvents(String email);
}
