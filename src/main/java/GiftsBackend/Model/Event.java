package GiftsBackend.Model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "Events")
public class Event {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String imageUrl;
    @OneToMany(mappedBy = "event")
    private List<Product> product;
    private String Location;
    private String details;
   private String category;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
