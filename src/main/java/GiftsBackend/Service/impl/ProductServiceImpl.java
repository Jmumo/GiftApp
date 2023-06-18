package GiftsBackend.Service.impl;

import GiftsBackend.Dtos.ProductDto;
import GiftsBackend.Model.Product;
import GiftsBackend.Repository.ProductRepository;
import GiftsBackend.Service.ProductService;
import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final Cloudinary cloudinary;
    private final ProductRepository productRepository;



    @Override
    public Product addProduct(ProductDto productDto, MultipartFile image) {

        String imageUrl;
        try {
            imageUrl = cloudinary.uploader()
                    .upload(image.getBytes(),
                            Map.of("public_id", UUID.randomUUID().toString())).get("url").toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        var product = Product.builder()
                .attributes(productDto.getAttributes())
                .Price(productDto.getPrice())
                .Name(productDto.getName())
                .createdDate(LocalDate.now())
                .ImageUrl(imageUrl)
                .build();
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> fetchProduct(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> fetchProducts() {
        return productRepository.findAll();
    }
}
