package GiftsBackend.Config.Auth;


import GiftsBackend.Config.JwtService;
import GiftsBackend.Dtos.AuthenticationRequest;
import GiftsBackend.Dtos.AuthenticationResponse;
import GiftsBackend.Dtos.RegisterRequest;
import GiftsBackend.Model.Role;
import GiftsBackend.Model.User;
import GiftsBackend.Repository.TokenRepo;
import GiftsBackend.Repository.UserRepository;
import GiftsBackend.Token.Token;
import GiftsBackend.Token.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepo tokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstName())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();
       var savedUser =  userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);

        savedUserToken(savedUser, jwtToken);

        return AuthenticationResponse.builder()
                .token(jwtToken)
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
        revokeAllUserToken(user);
        savedUserToken(user,jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
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
}
