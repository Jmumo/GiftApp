package GiftsBackend.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class Converter implements AttributeConverter<Map<String, String>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, String> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            // Handle the exception appropriately
            throw new RuntimeException("Failed to convert Map to JSON", e);
        }
    }

    @Override
    public Map<String, String> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, Map.class);
        } catch (IOException e) {
            // Handle the exception appropriately
            throw new RuntimeException("Failed to convert JSON to Map", e);
        }
    }
}
