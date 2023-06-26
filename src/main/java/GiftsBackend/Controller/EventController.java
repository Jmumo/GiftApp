package GiftsBackend.Controller;


import GiftsBackend.Dtos.EventDto;
import GiftsBackend.Dtos.ImageResponseDto;
import GiftsBackend.Dtos.ProductEventDto;
import GiftsBackend.Model.Event;
import GiftsBackend.Model.EventCategory;
import GiftsBackend.Service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/events")
public class EventController {
    private final EventService eventService;


    @PostMapping("/save")
    public ResponseEntity<EventDto> saveEvent(@RequestBody EventDto eventDto
                                           ){
        System.out.println("inside controller");
        System.out.println(eventDto.getUserEmail());
        return ResponseEntity.ok(eventService.addEvent(eventDto));
    }

    @GetMapping("/user/events/{email}")
    public ResponseEntity<List<Event>> getUserEvents(@PathVariable String email){
        return ResponseEntity.ok(eventService.getUserEvents(email));
    }

    @PostMapping("/uploadimage")
    public ResponseEntity<ImageResponseDto> uploadImage(@RequestParam("image") MultipartFile image
                                                                 ) throws IOException {
        return ResponseEntity.ok(eventService.UpdateProfileImage(image));
    }


    @PostMapping("/saveEventCategory")
    public ResponseEntity<EventCategory> saveEventCategory(
            @RequestParam("image") MultipartFile image,
            @RequestParam("name") String name
    ){
        return ResponseEntity.ok(eventService.saveEventCategory(image,name));
    }

    @PutMapping("/{eventId}/event/{productId}/product")
    public ResponseEntity<Event>addProductToEvent(
            @PathVariable Long eventId,
            @PathVariable Long productId
    ){
        return ResponseEntity.ok(eventService.addproductToEvent(eventId,productId));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEvent(id));
    }
}
