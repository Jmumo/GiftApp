package GiftsBackend.Dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SaveUSerEventDto {

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long productId;
    private String userEmail;

}
