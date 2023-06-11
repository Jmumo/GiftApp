package GiftsBackend.Controller;

import GiftsBackend.Dtos.ProductDto;
import GiftsBackend.Model.Event;
import GiftsBackend.Model.Product;
import GiftsBackend.Service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(
          @RequestParam("payload") String payload,
          @RequestParam("image") MultipartFile image
    ) throws JsonProcessingException {
        System.out.println(payload);
        ObjectMapper objectMapper = new ObjectMapper();
        ProductDto productDto = objectMapper.readValue(payload,ProductDto.class);
        System.out.println(productDto.toString());
        return ResponseEntity.ok(productService.addProduct(productDto,image));
    }

}
