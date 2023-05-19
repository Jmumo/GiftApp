package GiftsBackend.Dtos;


import GiftsBackend.Model.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {

    private String firstName;
    private String lastname;
    private String email;
    private String phoneNumber;
    private LocalDate birthDayDate;
    private String password;

}
