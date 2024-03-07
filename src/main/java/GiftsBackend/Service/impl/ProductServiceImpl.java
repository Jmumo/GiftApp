package GiftsBackend.Service.impl;

import GiftsBackend.Dtos.ProductDto;
import GiftsBackend.Dtos.ProductsSearchResposnse;
import GiftsBackend.Execptions.UserNotFoundException;
import GiftsBackend.Model.Product;
import GiftsBackend.Model.User;
import GiftsBackend.Repository.ProductRepository;
import GiftsBackend.Repository.UserRepository;
import GiftsBackend.Service.ProductService;
import GiftsBackend.Service.ProfileService;
import GiftsBackend.Utils.HelperUtility;
import com.cloudinary.Cloudinary;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.jpa.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
    private final ObjectMapper objectMapper;

    private final ProfileService profileService;

    private final HelperUtility helperUtility;




    private final EntityManager entityManager;



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

        if(profileService.getCurrentUser().isEnabled()){
            return  profileService.getCurrentUser().getWishlist();
        }
        return new HashSet<>();
    }

    @Override
    @Transactional
    public Set<Product> addToUserWishList(Long productId) {

        User user = helperUtility.getCurrentUser();
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
    public ProductsSearchResposnse searchProduct( String name,
                                        String priceDirection,
                                        String dateFilterDirection,
                                        String Brand,
                                        Integer pageNumber,
                                        Integer pageSize
                                       ) {


        List<Product> products = searchAndFilterProducts(name, priceDirection, dateFilterDirection, Brand, pageNumber, pageSize);


        ProductsSearchResposnse productsSearchResposnse = new ProductsSearchResposnse();
        productsSearchResposnse.setCurrentPage(pageNumber);
        productsSearchResposnse.setNextPage(pageNumber + 1);
        productsSearchResposnse.setProductList(products);
        if(pageNumber > 0){
            productsSearchResposnse.setPreviousPage(pageNumber-1);
        }
        productsSearchResposnse.setPreviousPage(0);

        return productsSearchResposnse;

    }

    @Override
    public Set<Product> removeToUserWishList(Long productId) {
        User user = helperUtility.getCurrentUser();
        Optional<Product> product = productRepository.findById(productId);


        if(product.isPresent()){
            product.get().setUser(user);
            user.getWishlist().remove(product.get());
            userRepository.save(user);
            return new HashSet<>(user.getWishlist());
        }
        return new HashSet<>();
    }





   private List<Product> searchAndFilterProducts(
            String Name,
            String priceDirection,
            String dateFilterDirection,
            String Brand,
            Integer pageNumber,
            Integer pageSize
    ){

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();

       if(Name != null){
           predicates.add(criteriaBuilder.like(root.get("Name"),"%"+Name+"%"));

       }

       if(dateFilterDirection != null){
           if(dateFilterDirection.equals("ASC")) {
               criteriaQuery.orderBy(criteriaBuilder.asc(root.get("createdDate")));
           }else{
               criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdDate")));
           }


       }

       if(priceDirection != null){
           if(priceDirection.equals("HIGHEST")){
               criteriaQuery.orderBy((criteriaBuilder.desc(root.get("Price"))));
           }else {
               criteriaQuery.orderBy((criteriaBuilder.asc(root.get("Price"))));
           }


       }

        if(Brand != null){
            predicates.add(criteriaBuilder.equal(root.get("Brand"),Brand));
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));


        TypedQuery<Product> typedQuery = entityManager.createQuery(criteriaQuery);
        // Enable logging for this query
        typedQuery.setHint(QueryHints.HINT_COMMENT, "Generated SQL: " + criteriaQuery.toString());


        PageRequest pageRequest = PageRequest.of(pageNumber,pageSize);

        // Print the generated SQL query
        System.out.println("Generated SQL Query: " + typedQuery.unwrap(org.hibernate.query.Query.class).getQueryString());

       return entityManager.createQuery(criteriaQuery)
               .setFirstResult((int) pageRequest.getOffset())
               .setMaxResults(pageRequest.getPageSize())
               .getResultList();

   }
}
