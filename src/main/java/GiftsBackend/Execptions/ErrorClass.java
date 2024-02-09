package GiftsBackend.Execptions;


import lombok.Data;

@Data
public class ErrorClass {
    private String ErrorCode;
    private String ErrorMessage;
}
