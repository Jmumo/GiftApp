package GiftsBackend.Utils;

import GiftsBackend.Execptions.UserNotFoundException;
import GiftsBackend.Model.User;
import GiftsBackend.Repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class HelperUtility {


    private final UserRepository userRepository;

    public HelperUtility(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static String toBase64String(String value) {
        // Convert the input string to bytes
        byte[] inputBytes = value.getBytes();

        // Encode the input bytes to Base64
        byte[] encodedBytes = Base64.getEncoder().encode(inputBytes);

        // Convert the encoded bytes back to a string
        return new String(encodedBytes);

    }

    public static String toJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException exception) {
            return null;
        }
    }

    public static String GeneratePaymentRef(){
        int length = 10;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder randomString = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }

        return randomString.toString();
    }

    public static String getStkPushPassword(String shortCode, String passKey, String timestamp) {
        String concatenatedString = String.format("%s%s%s", shortCode, passKey, timestamp);
        return toBase64String(concatenatedString);
    }

    public static String getTransactionTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date());
    }


    public User getCurrentUser() throws UserNotFoundException {
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(user.isPresent()){
            return user.get();
        }
        throw new UserNotFoundException(user.get().getEmail());
    }








}
