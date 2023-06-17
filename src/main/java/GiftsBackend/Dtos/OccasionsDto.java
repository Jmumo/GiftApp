package GiftsBackend.Dtos;


import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OccasionsDto {
    private Long id;
    private String name;
    private String ImageUrl;
}
