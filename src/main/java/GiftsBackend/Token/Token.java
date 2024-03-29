package GiftsBackend.Token;


import GiftsBackend.Model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "UserTokens")
public class Token {
    @Id
    @GeneratedValue
    private Long id;

    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokentype;

    private boolean expired;

    private boolean revoked;

    private boolean refresh;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
