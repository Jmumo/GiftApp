package GiftsBackend.Dtos;


import GiftsBackend.Model.UserProfile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @NotNull(message = "First name cannot be null")
    @NotBlank(message = "First name cannot be blank")
    private String firstName;
    @NotNull(message = "Last name cannot be null")
    @NotBlank(message = "Last name cannot be blank")
    private String lastname;
    @NotNull(message = "email name cannot be null")
    @NotBlank(message = "email name cannot be blank")
    private String email;
    @NotNull(message = "phoneNumber name cannot be null")
    @NotBlank(message = "phoneNumber name cannot be blank")
    private String phoneNumber;
    @NotNull(message = "birthDayDate name cannot be null")
    @NotBlank(message = "birthDayDate name cannot be blank")
    private LocalDate birthDayDate;
    @NotNull(message = "password name cannot be null")
    @NotBlank(message = "password name cannot be blank")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
//    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$")
    private String password;

}
