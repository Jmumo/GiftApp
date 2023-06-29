package GiftsBackend.Dtos;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class EventDto {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private String category;
    private String details;
    private Long productId;
    private String userEmail;
    private String imageUrl;
    private BigDecimal cost;
    private String color;

}
