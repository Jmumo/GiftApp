package GiftsBackend.Dtos;


import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OccasionsDto {
    private String name;
    private String ImageUrl;
}
