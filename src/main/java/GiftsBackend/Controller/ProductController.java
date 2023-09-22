package GiftsBackend.Controller;

import GiftsBackend.Dtos.ProductDto;
import GiftsBackend.Model.Product;
import GiftsBackend.Service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;


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
        ObjectMapper objectMapper = new ObjectMapper();
        ProductDto productDto = objectMapper.readValue(payload,ProductDto.class);
        return ResponseEntity.ok(productService.addProduct(productDto,image));
    }


    @GetMapping("/fetchProduct/{id}")
    public ResponseEntity<Optional<Product>> fetchProduct(@PathVariable Long id){
        return ResponseEntity.ok(productService.fetchProduct(id));
    }

    @GetMapping("/fetchProducts")
    public ResponseEntity<List<Product>>  fetchProducts(){
        return ResponseEntity.ok(productService.fetchProducts());
    }

    @GetMapping("/getUserWishlist")
    public ResponseEntity<Set<Product>> wishList(){
        return ResponseEntity.ok(productService.fetchUserWishList());
    }


    @GetMapping("/getUserWishlist/{productId}")
    public ResponseEntity<Set<Product>> getUserWishList(@PathVariable Long productId ){
        return ResponseEntity.ok(productService.addToUserWishList(productId));
    }

}
