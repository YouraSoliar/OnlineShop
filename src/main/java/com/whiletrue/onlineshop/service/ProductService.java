package com.whiletrue.onlineshop.service;

import com.whiletrue.onlineshop.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product createProduct(Product Product);
    Optional<Product> getProductById(Long id);
    List<Product> getAllProduct();
    void deleteProduct(Long id);
}
