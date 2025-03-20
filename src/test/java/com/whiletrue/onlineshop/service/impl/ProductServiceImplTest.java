package com.whiletrue.onlineshop.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.whiletrue.onlineshop.model.Product;
import com.whiletrue.onlineshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product1.setId(1L);
        product1.setTitle("Coca Cola");
        product1.setDescription("2 liters, Cherry taste");
        product1.setPrice(40.0);

        product2 = new Product();
        product2.setId(2L);
        product2.setTitle("Fanta");
        product2.setDescription("2 liters, Zero Sugar");
        product2.setPrice(44.0);
    }

    @Test
    void testCreateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product1);

        Product createdProduct = productService.createProduct(product1);

        assertNotNull(createdProduct);
        assertEquals("Coca Cola", createdProduct.getTitle());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testGetProductById_ProductExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        Optional<Product> retrievedProduct = productService.getProductById(1L);

        assertTrue(retrievedProduct.isPresent());
        assertEquals(1L, retrievedProduct.get().getId());
    }

    @Test
    void testGetProductById_ProductNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Product> retrievedProduct = productService.getProductById(99L);

        assertFalse(retrievedProduct.isPresent());
    }

    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> products = productService.getAllProduct();

        assertEquals(2, products.size());
        verify(productRepository).findAll();
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository).deleteById(1L);
    }
}