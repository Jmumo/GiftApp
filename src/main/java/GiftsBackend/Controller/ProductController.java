package GiftsBackend.Controller;

import GiftsBackend.Dtos.ProductDto;
import GiftsBackend.Model.Product;
import GiftsBackend.Service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


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
    public ResponseEntity<Set<Product>> getWishList(){
        return ResponseEntity.ok(productService.fetchUserWishList());
    }


    @GetMapping("/addToUserWishlist/{productId}")
    public ResponseEntity<Set<Product>> addToUserWishList(@PathVariable Long productId ){
        return ResponseEntity.ok(productService.addToUserWishList(productId));
    }



    @GetMapping("/removeFromUserWishlist/{productId}")
    public ResponseEntity<Set<Product>> removeToUserWishList(@PathVariable Long productId ){
        return ResponseEntity.ok(productService.removeToUserWishList(productId));
    }


    @GetMapping("/searchProducts")
    public List<Product> searchCard(@RequestParam(required = false) String name,
                                    @RequestParam(required = false) String priceDirection,
                                    @RequestParam(required = false) LocalDate dateFilterDirection,
                                    @RequestParam(required = false) String Brand,
                                    @RequestParam(required = false) Integer pageNumber,
                                    @RequestParam(required = false) Integer pageSize
    ){
        return productService.searchProduct(name, priceDirection, dateFilterDirection, Brand, pageNumber,pageSize);

    }

}
