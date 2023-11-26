package GiftsBackend.Controller;


import GiftsBackend.Dtos.*;
import GiftsBackend.Model.Event;
import GiftsBackend.Model.EventCategory;
import GiftsBackend.Model.Product;
import GiftsBackend.Service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<Event> saveEvent(@RequestBody EventDto eventDto
    ) {
        System.out.println("hello");
        return ResponseEntity.ok(eventService.addEvent(eventDto));
    }

    @GetMapping("/user/events/{email}")
    public ResponseEntity<List<Event>> getUserEvents(@PathVariable String email) {
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
    ) {
        return ResponseEntity.ok(eventService.saveEventCategory(image, name));
    }

    @PutMapping("/{eventId}/event/{productId}/product")
    public ResponseEntity<Event> addProductToEvent(
            @PathVariable Long eventId,
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(eventService.addproductToEvent(eventId, productId));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEvent(id));
    }


    @PutMapping("/confirm")
    public ResponseEntity<Event> confirmEvent(@RequestBody ConfirmEventDto confirmEventDto){
        return ResponseEntity.ok(eventService.confirm(confirmEventDto));
    }

    @PutMapping
    public ResponseEntity<Event> contributeToEvent(@RequestBody ContributeEventDto contributeEventDto){
        return ResponseEntity.ok(eventService.contributeToEvent(contributeEventDto));
    }

    @GetMapping("/calendarEvents")
    public ResponseEntity<List<Event>> getCalendarEvents( @RequestParam LocalDate startDate, @RequestParam LocalDate endDate){
        System.out.println("at the controller");
        return ResponseEntity.ok(eventService.getCalendarEvents(startDate,endDate));
    }



    @GetMapping("/searchEvents")
    public List<Event> searchCard(  @RequestParam(required = false) String name,
                                    @RequestParam(required = false) String sort,
                                    @RequestParam(required = false) Integer pageNumber,
                                    @RequestParam(required = false) Integer pageSize,
                                    @RequestParam(required = false) Sort.Direction Sortdirection
    ){
        return eventService.searchProduct(name,sort,pageNumber,pageSize,Sortdirection);
    }
}
