package GiftsBackend.Config.Auth;


import GiftsBackend.Config.JwtService;
import GiftsBackend.Dtos.AuthenticationRequest;
import GiftsBackend.Dtos.AuthenticationResponse;
import GiftsBackend.Dtos.RegisterRequest;
import GiftsBackend.Execptions.DuplicateUserExeption;
import GiftsBackend.Model.Role;
import GiftsBackend.Model.User;
import GiftsBackend.Model.UserProfile;
import GiftsBackend.Repository.TokenRepo;
import GiftsBackend.Repository.UserRepository;
import GiftsBackend.Token.Token;
import GiftsBackend.Token.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepo tokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {

        Optional<User> Existinguser = userRepository.findByEmail(request.getEmail());


        if(!Existinguser.isEmpty()){
            try {
                throw new DuplicateUserExeption(Existinguser.get().getEmail());
            } catch (DuplicateUserExeption e) {
                throw new RuntimeException(e);
            }
        }

        UserProfile userProfile = new UserProfile();
        userProfile.setCreatedDate(LocalDateTime.now());
        var user = User.builder()
                .firstname(request.getFirstName())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .birthDayDate(request.getBirthDayDate())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .createdDate(LocalDateTime.now())
                .wishlist(new HashSet<>())
                .userProfile(userProfile)
                .build();


       var savedUser =  userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        savedUserToken(savedUser, jwtToken);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }




    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new UsernameNotFoundException("User Not Found for Authentication"));
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserToken(user);
        savedUserToken(user,jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void revokeAllUserToken(User user){
        var validUserTokens = tokenRepo.findAllValidTokenByUser(user.getId());
        if(validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });
        tokenRepo.saveAll(validUserTokens);
    }


    private void savedUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .tokentype(TokenType.BEARER)
                .user(user)
                .build();
        tokenRepo.save(token);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);

        userEmail = jwtService.extractUsername(refreshToken);

        if(userEmail !=null){

           var  user = this.userRepository.findByEmail(userEmail).orElseThrow();

            if(jwtService.isValid(refreshToken,user)){
                var token = jwtService.generateToken(user);
                revokeAllUserToken(user);
                savedUserToken(user,token);
                 var authResponse = AuthenticationResponse.builder()
                         .token(token)
                         .refreshToken(refreshToken)
                         .build();

                 new ObjectMapper().writeValue(response.getOutputStream(),authResponse);
            }

        }
    }
}
