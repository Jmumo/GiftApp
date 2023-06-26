package GiftsBackend.Utils;

import lombok.extern.slf4j.Slf4j;



import java.nio.charset.StandardCharsets;
import java.util.Base64;



public class HelperUtility {

    public static String toBase64String(String value) {
        // Convert the input string to bytes
        byte[] inputBytes = value.getBytes();

        // Encode the input bytes to Base64
        byte[] encodedBytes = Base64.getEncoder().encode(inputBytes);

        // Convert the encoded bytes back to a string
        return new String(encodedBytes);

    }
}
