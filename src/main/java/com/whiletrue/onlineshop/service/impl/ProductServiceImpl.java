package com.whiletrue.onlineshop.service.impl;

import com.whiletrue.onlineshop.model.Product;
import com.whiletrue.onlineshop.repository.ProductRepository;
import com.whiletrue.onlineshop.service.ProductService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @CachePut(value = "products", key = "#product.id")
    public Product createProduct(Product product) {
        Product createdProduct = productRepository.save(product);
        logger.info("Product created successfully with ID: {}", createdProduct.getId());
        return createdProduct;
    }

    @Override
    @Cacheable(value = "products", key = "#id")
    public Optional<Product> getProductById(Long id) {
        Optional<Product> getProduct = productRepository.findById(id);
        logger.info("Is product with ID: {} - {}", id, getProduct.isPresent());
        return getProduct;
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
        logger.info("Product with ID {} deleted successfully", id);
    }
}
