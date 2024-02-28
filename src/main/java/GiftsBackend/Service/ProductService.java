package GiftsBackend.Service;

import GiftsBackend.Dtos.ProductDto;
import GiftsBackend.Dtos.ProductsSearchResposnse;
import GiftsBackend.Model.Product;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductService {
    Product addProduct(ProductDto productDto, MultipartFile image);

    Optional<Product> fetchProduct(Long id);

    List<Product> fetchProducts();

    Set<Product> fetchUserWishList();

    Set<Product> addToUserWishList( Long productId);

    ProductsSearchResposnse searchProduct(
            String name,
            String priceDirection,
            String dateFilterDirection,
            String Brand,
            Integer pageNumber,
            Integer pageSize
    );

    Set<Product> removeToUserWishList(Long productId);
}
