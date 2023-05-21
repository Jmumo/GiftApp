package GiftsBackend.Model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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
    //    @OneToMany
    private Long productId;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
