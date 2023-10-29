package GiftsBackend.Model;


import GiftsBackend.Token.Token;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "AppUsers")
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private Long id;
    private String firstname;
    private String lastname;
    @Column(nullable = false,unique = true)
    private String email;
    @Column(nullable = false,unique = true)
    private String phoneNumber;
    private LocalDate birthDayDate;
    private LocalDateTime createdDate;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
     @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "user_profile_id",referencedColumnName = "id")
    private UserProfile userProfile;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Event> events;


    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    private Set<Product> wishlist;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", birthDayDate=" + birthDayDate +
                ", createdDate=" + createdDate +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
