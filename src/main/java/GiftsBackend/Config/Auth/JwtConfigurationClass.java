package GiftsBackend.Config.Auth;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "app.security.jwt")
public record JwtConfigurationClass( String secretKey,Long expiration,Long refreshtokenexpiration) {
}
