package GiftsBackend.Service;

import GiftsBackend.Dtos.OccasionsDto;
import GiftsBackend.Model.EventType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventTypeService {

    OccasionsDto save(MultipartFile image, String name);

    List<EventType> findGetEvents();
}
