package GiftsBackend.Execptions;

public class DuplicateUserExeption extends RuntimeException {
    public DuplicateUserExeption(String email) {
        super("User with The Following email is existing: " + email);
    }
}
