package GiftsBackend.Dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterUrlRequest {

    @JsonProperty("ShortCode")
    private String ShortCode;

    @JsonProperty("ConfirmationURL")
    private String ConfirmationURL;

    @JsonProperty("ValidationURL")
    private String ValidationURL;

    @JsonProperty("ResponseType")
    private String ResponseType;
}
