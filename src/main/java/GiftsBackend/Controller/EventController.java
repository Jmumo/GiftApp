package GiftsBackend.Controller;


import GiftsBackend.Dtos.SaveUSerEventDto;
import GiftsBackend.Model.Event;
import GiftsBackend.Service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/events")
public class EventController {
    private final EventService eventService;

    @PostMapping("/save")
    public ResponseEntity<Event> saveEvent(@RequestBody SaveUSerEventDto saveUSerEventDto){
        return ResponseEntity.ok(eventService.addEvent(saveUSerEventDto));
    }

    @GetMapping("/user/events/{email}")

    public ResponseEntity<List<Event>> getUserEvents(@PathVariable String email){
        return ResponseEntity.ok(eventService.getUserEvents(email));
    }


}
