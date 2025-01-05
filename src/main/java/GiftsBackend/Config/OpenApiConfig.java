package GiftsBackend.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;


@OpenAPIDefinition(info = @Info(title = "some title", description = "API for <project name> endpoints. For DataTransfer and Logs endpoints, please contact dev team.", version = "v0.1"),
        servers = {
                @Server(
                        description = "localhost",
                        url = "http://localhost:8080"
                ),
//                @Server(
//                        description = "dev server",
//                        url = "https://<your-server-here>/"
//                )
        }
)

public class OpenApiConfig {

}
