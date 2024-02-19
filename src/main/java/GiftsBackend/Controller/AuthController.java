package GiftsBackend.Controller;


import GiftsBackend.Config.Auth.AuthenticationService;
import GiftsBackend.Dtos.AuthenticationRequest;
import GiftsBackend.Dtos.AuthenticationResponse;
import GiftsBackend.Dtos.RefreshTokenRequest;
import GiftsBackend.Dtos.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request){
       return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

//    @PostMapping("/refreshToken")
//    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
//    authenticationService.refreshToken(request,response);
//    }


    @PostMapping("/refreshToken")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request) throws IOException {
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }


}
