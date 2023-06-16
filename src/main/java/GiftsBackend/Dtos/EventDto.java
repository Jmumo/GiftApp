package GiftsBackend.Dtos;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class EventDto {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
     private String location;
   private String category;
   private String details;
  private Long productId;
    private String userEmail;
    private String imageUrl;

}
