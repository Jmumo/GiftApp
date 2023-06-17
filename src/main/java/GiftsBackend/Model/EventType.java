package GiftsBackend.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;



@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class EventType {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String imageUrl;
}
