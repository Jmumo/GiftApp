package GiftsBackend.Service;

import GiftsBackend.Dtos.ProductDto;
import GiftsBackend.Model.Product;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    Product addProduct(ProductDto productDto, MultipartFile image);

}
