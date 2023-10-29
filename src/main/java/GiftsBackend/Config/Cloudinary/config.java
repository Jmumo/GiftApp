package GiftsBackend.Config.Cloudinary;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class config {

   private final CloudinaryConfigurationsClass configurationsClass;

    public config(CloudinaryConfigurationsClass configurationsClass) {
        this.configurationsClass = configurationsClass;
    }

    @Bean
    public Cloudinary cloudinary(){
        Map<String,String> config = new HashMap<>();
        config.put("cloud_name", configurationsClass.CLOUD_NAME());
        config.put("api_key", configurationsClass.API_KEY());
        config.put("api_secret", configurationsClass.API_SECRET());
        return new Cloudinary(config);
    }
}
