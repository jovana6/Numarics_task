package com.assignment.demoproject;

import com.assignment.demoproject.productservice.Product;
import com.assignment.demoproject.productservice.ProductRepository;
import com.assignment.demoproject.productservice.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    public ProductServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Product", "Description", "Category"));
        products.add(new Product(2L, "Product1", "Description1", "Category1"));

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
    }

    @Test
    void getProductById_ExistingId_ShouldReturnProduct() {
        Product product = new Product(1L, "Product", "Description", "Category");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProductById(1L);

        assertTrue(result.isPresent());
        assertEquals("Product", result.get().getName());
    }

    @Test
    void getProductById_NonExistingId_ShouldReturnEmptyOptional() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Product> result = productService.getProductById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct() {
        Product productToCreate = new Product(null, "New_Product", "New_Description", "New_Category");
        Product createdProduct = new Product(1L, "New_Product", "New_Description", "New_Category");

        when(productRepository.save(any(Product.class))).thenReturn(createdProduct);

        Product result = productService.createProduct(productToCreate);

        assertEquals(1L, result.getId());
        assertEquals("New_Product", result.getName());
    }

    @Test
    void updateProduct_ExistingId_ShouldReturnUpdatedProduct() {
        Product updatedProduct = new Product(1L, "Updated_Product", "Updated_Description", "Updated_Category");

        when(productRepository.existsById(1L)).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        Product result = productService.updateProduct(1L, updatedProduct);

        assertEquals("Updated_Product", result.getName());
        Mockito.verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProduct_NonExistingId_ShouldThrowEntityNotFoundException() {
        when(productRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> productService.updateProduct(1L, new Product()));
        Mockito.verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void searchProducts_ShouldReturnFilteredProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Product1", "Description1", "Category1"));
        products.add(new Product(2L, "Product2", "Description2", "Category2"));

        when(productRepository.filterProducts(
                "Product", "Description", "Category"))
                .thenReturn(products);

        List<Product> result = productService.searchProducts("Product", "Description", "Category");

        assertEquals(2, result.size());
    }
}
