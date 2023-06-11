package GiftsBackend.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Product {
    @Id
    @GeneratedValue
    private Long Id;
    private String Name;
    private double Price;
    private String ImageUrl;
    private LocalDate createdDate;
    @Convert(converter = Converter.class)
    private Map<String, String> attributes;
    @ManyToOne
    private Event event;

}
