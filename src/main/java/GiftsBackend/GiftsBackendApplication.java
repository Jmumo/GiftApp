package GiftsBackend;

import GiftsBackend.Config.Auth.JwtConfigurationClass;
import GiftsBackend.Config.Cloudinary.CloudinaryConfigurationsClass;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({CloudinaryConfigurationsClass.class, JwtConfigurationClass.class})
public class GiftsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(GiftsBackendApplication.class, args);
	}

}
