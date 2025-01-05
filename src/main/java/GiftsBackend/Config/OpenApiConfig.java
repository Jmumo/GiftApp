package GiftsBackend.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;


@OpenAPIDefinition(info = @Info(title = "Friends Gift App", description = "API for <project name> endpoints. For DataTransfer and Logs endpoints, please contact dev team.", version = "v0.1"),
        servers = {
                @Server(
                        description = "localhost",
                        url = "http://localhost:8080"
                ),
//
        },

        security = @SecurityRequirement(
                name = "GiftApp Security Scheme"
        )
)


@SecurityScheme(
        name = "GiftApp Security Scheme",
        description = "Gift app Security description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

}
