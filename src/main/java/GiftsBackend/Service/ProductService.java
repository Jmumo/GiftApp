package GiftsBackend.Service;

import GiftsBackend.Dtos.ProductDto;
import GiftsBackend.Model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product addProduct(ProductDto productDto, MultipartFile image);

    Optional<Product> fetchProduct(Long id);

    List<Product> fetchProducts();

}
