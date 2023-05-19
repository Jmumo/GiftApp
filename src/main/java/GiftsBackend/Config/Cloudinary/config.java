package GiftsBackend.Config.Cloudinary;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class config {
    @Value("${app.cloudinary.API_KEY}")
    private String API_KEY;
    @Value("${app.cloudinary.API_SECRET}")
    private String API_SECRET;
    @Value("${app.cloudinary.CLOUD_NAME}")
    private String CLOUD_NAME;



    @Bean
    public Cloudinary cloudinary(){
        Map<String,String> config = new HashMap<>();
        config.put("cloud_name",CLOUD_NAME);
        config.put("api_key",API_KEY);
        config.put("api_secret",API_SECRET);
        return new Cloudinary(config);
    }
}
