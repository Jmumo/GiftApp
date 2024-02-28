package GiftsBackend.Dtos;

import GiftsBackend.Model.Product;
import lombok.Data;

import java.util.List;

@Data
public class ProductsSearchResposnse {
    private List<Product> productList;
    private Integer nextPage;
    private Integer currentPage;
    private Integer previousPage;
}
