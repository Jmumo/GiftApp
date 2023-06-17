package GiftsBackend.Controller;


import GiftsBackend.Dtos.OccasionsDto;
import GiftsBackend.Model.EventType;
import GiftsBackend.Service.EventTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/Occasions")
public class EventTypeController {

    private final EventTypeService eventTypeService;


    @PostMapping
    public ResponseEntity<OccasionsDto> saveOccasion(@RequestParam MultipartFile image, @RequestParam String name
    ){
        return  ResponseEntity.ok(eventTypeService.save(image,name));
    }


    @GetMapping
    public ResponseEntity<List<EventType>> getOccasions(){
        return ResponseEntity.ok(eventTypeService.findGetEvents());
    }

}
