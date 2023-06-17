package GiftsBackend.Service.impl;


import GiftsBackend.Dtos.OccasionsDto;
import GiftsBackend.Model.EventType;
import GiftsBackend.Repository.EvetypeRepository;
import GiftsBackend.Service.EventTypeService;
import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventTypeServiceImpl implements EventTypeService {

    private final EvetypeRepository evetypeRepository;
    private final Cloudinary cloudinary;
    @Override
    public OccasionsDto save(MultipartFile image,String name) {



        String imageUrl;
        try {
            imageUrl = cloudinary.uploader()
                    .upload(image.getBytes(),
                            Map.of("public_id", UUID.randomUUID().toString())).get("url").toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var eventType = EventType.builder().name(name).imageUrl(imageUrl).build();

        var ReturnEvent = evetypeRepository.save(eventType);

        return OccasionsDto.builder().name(ReturnEvent.getName()).ImageUrl(ReturnEvent.getImageUrl()).build();
    }

    @Override
    public List<EventType> findGetEvents() {
        return evetypeRepository.findAll();

    }
}
