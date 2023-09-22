package GiftsBackend.Execptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String email) {
        super("User with The Following email is not found: " + email);
    }
}
