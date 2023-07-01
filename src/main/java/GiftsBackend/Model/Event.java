package GiftsBackend.Model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.GenerationType.SEQUENCE;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "Tbl_Events")
public class Event {
    @Id
    @SequenceGenerator(
            name = "Event_Sequence",
            sequenceName = "Event_Sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "Event_Sequence"
    )
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String color;
    private LocalTime startTime;
    private LocalTime endTime;
    private String imageUrl;
    @ManyToMany
    @JoinTable(name = "Events_Products",
    joinColumns = @JoinColumn(name = "event_id"),
    inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> products = new HashSet<>();
    private String Location;
    private String details;
    private String category;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private BigDecimal cost;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;

}
