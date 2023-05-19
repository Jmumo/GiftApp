package GiftsBackend.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "UsersProfile")
public class UserProfile {
    @Id
    @GeneratedValue
    private Long id;
    private String Elias;
    private LocalDateTime createdDate;
    private String about;
    private String type;
    private String imageUrl;

}
