package GiftsBackend.Config.Cloudinary;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "app.cloudinary")
public record CloudinaryConfigurationsClass(String API_KEY, String API_SECRET, String CLOUD_NAME ) {
}
