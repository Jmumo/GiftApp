package GiftsBackend.Config.Auth;

import GiftsBackend.Execptions.ErrorClass;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.SignatureException;
import java.util.Objects;


@Component
//@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        ErrorClass error = new ErrorClass();
        String errorMessage;
        error.setErrorCode("401");

        Throwable rootCause = authException.getCause();


        if (rootCause instanceof ExpiredJwtException) {
            errorMessage = "Expired token";
        } else if (rootCause instanceof MalformedJwtException) {
            errorMessage = "Malformed token";
        } else if (rootCause instanceof SignatureException) {
            errorMessage = "Invalid token signature";
        } else {
            errorMessage = "Unauthorized The Token Is Either Being used By another App or your already logged out";
        }


        error.setErrorMessage(errorMessage);

        ObjectMapper mapper = new ObjectMapper();
        String jsonError = mapper.writeValueAsString(error);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(jsonError);
    }
}
