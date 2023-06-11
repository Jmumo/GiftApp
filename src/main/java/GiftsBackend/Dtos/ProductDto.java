package GiftsBackend.Dtos;


import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private double price;
    private Map<String, String> attributes;
}
