package GiftsBackend.Dtos;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Map<String, String> attributes;
}
