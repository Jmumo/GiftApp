package GiftsBackend.Service.impl;

import GiftsBackend.Dtos.ProductDto;
import GiftsBackend.Execptions.UserNotFoundException;
import GiftsBackend.Model.Product;
import GiftsBackend.Model.User;
import GiftsBackend.Repository.ProductRepository;
import GiftsBackend.Repository.UserRepository;
import GiftsBackend.Service.ProductService;
import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final Cloudinary cloudinary;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;



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
                .Description(productDto.getDescription())
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

    @Override
    public Set<Product> fetchUserWishList() {

        if(getCurrentUser().isEnabled()){
            return  getCurrentUser().getWishlist();
        }
        return new HashSet<>();
    }

    @Override
    public Set<Product> addToUserWishList(Long productId) {
        return getCurrentUser().getWishlist();
    }

    private User getCurrentUser(){
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(user.isPresent()){
            return user.get();
        }
        throw new UserNotFoundException(user.get().getEmail());
    }
}
