package GiftsBackend.Service.impl;

import GiftsBackend.Dtos.ProductDto;
import GiftsBackend.Execptions.UserNotFoundException;
import GiftsBackend.Model.Product;
import GiftsBackend.Model.User;
import GiftsBackend.Repository.ProductRepository;
import GiftsBackend.Repository.UserRepository;
import GiftsBackend.Service.ProductService;
import GiftsBackend.Utils.SearchSpecifications;
import com.cloudinary.Cloudinary;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final Cloudinary cloudinary;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final SearchSpecifications<Product> filterSpecifications;



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
    @Transactional
    public Set<Product> addToUserWishList(Long productId) {

        User user = getCurrentUser();
        Optional<Product> product = productRepository.findById(productId);


        if(product.isPresent()){
            product.get().setUser(user);
            user.getWishlist().add(product.get());
            userRepository.save(user);
            return new HashSet<>(user.getWishlist());
        }
        return new HashSet<>();

    }

    @Override
    public List<Product> searchProduct(String name, String sort, Integer pageNumber, Integer pageSize, Sort.Direction sortdirection) {

        Specification<Product> cardSpecification = filterSpecifications.searchSpecification(name);

        if(pageNumber == null){
            pageNumber = 0;
        }

        if(pageSize == null){
            pageSize =1;
        }
        if(sort == null){
            sort = "createdDate";
        }

        if(sortdirection == null){
            sortdirection = Sort.Direction.ASC;
        }else {
            sortdirection = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortdirection,sort));

        return productRepository.findAll(cardSpecification,pageable);
    }

    @Override
    public Set<Product> removeToUserWishList(Long productId) {
        User user = getCurrentUser();
        Optional<Product> product = productRepository.findById(productId);


        if(product.isPresent()){
            product.get().setUser(user);
            user.getWishlist().remove(product.get());
            userRepository.save(user);
            return new HashSet<>(user.getWishlist());
        }
        return new HashSet<>();
    }

    private User getCurrentUser() throws UserNotFoundException {
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(user.isPresent()){
            return user.get();
        }
        throw new UserNotFoundException(user.get().getEmail());
    }
}
