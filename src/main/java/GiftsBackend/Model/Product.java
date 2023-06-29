package GiftsBackend.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private BigDecimal Price;
    private String ImageUrl;
    private LocalDate createdDate;
    @Convert(converter = Converter.class)
    private Map<String, String> attributes;
    @JsonIgnore
    @ManyToMany(mappedBy = "products")
    private Set<Event> event = new HashSet<>();

}
