package GiftsBackend.Controller;


import GiftsBackend.Dtos.ProductEventDto;
import GiftsBackend.Dtos.SaveEventCategoryDto;
import GiftsBackend.Dtos.SaveUSerEventDto;
import GiftsBackend.Model.Event;
import GiftsBackend.Model.EventCategory;
import GiftsBackend.Service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/events")
public class EventController {
    private final EventService eventService;

    @PostMapping("/save")
    public ResponseEntity<Event> saveEvent(@RequestParam("image") MultipartFile image,
                                           @RequestParam("name") String name,
                                           @RequestParam("startDate") LocalDate startDate,
                                           @RequestParam("endDate") LocalDate endDate,
                                           @RequestParam("location") String location,
                                           @RequestParam("category") String category,
                                           @RequestParam("details") String details,
                                           @RequestParam("productId") Long productId,
                                           @RequestParam("userEmail") String userEmail
                                           ){
        return ResponseEntity.ok(eventService.addEvent(image,name,startDate,endDate,location,category,details,productId,userEmail));
    }

    @GetMapping("/user/events/{email}")
    public ResponseEntity<List<Event>> getUserEvents(@PathVariable String email){
        return ResponseEntity.ok(eventService.getUserEvents(email));
    }


    @PostMapping("/saveEventCategory")
    public ResponseEntity<EventCategory> saveEventCategory(@RequestBody SaveEventCategoryDto saveEventCategoryDto){
        return ResponseEntity.ok(eventService.saveEventCategory(saveEventCategoryDto.getName()));
    }

    @PostMapping("/addProductToEvent")
    public ResponseEntity<Event> addProductToEvent(ProductEventDto productEventDto){
         return ResponseEntity.ok(eventService.addproductToEvent(productEventDto));
    }


}
