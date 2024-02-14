package GiftsBackend.Execptions;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException(String message){
        super("Invalid Token found get a new Token");
    }
}
