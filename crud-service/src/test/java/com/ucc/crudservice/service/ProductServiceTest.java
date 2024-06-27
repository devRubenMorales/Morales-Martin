package com.ucc.crudservice.service;

import com.ucc.crudservice.model.entities.Product;
import com.ucc.crudservice.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@SpringBootTest
public class ProductServiceTest {

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @BeforeEach
    void setUp (){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getProducts(){
        Product product = new Product(1L, "SKU001", "Product1", "Description1", 100.0, true);
        List<Product> products = Collections.singletonList(product);

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getProducts();
        assertEquals(1, result.size());
        assertEquals("SKU001", result.get(0).getSku());

    }


    @Test
    public void testDeleteProduct() {
        Long productId = 1L;

        // Ejecutar el Método a Probar
        ResponseEntity<Object> response = productService.deleteProduct(productId);

        // Verificar el Comportamiento
        verify(productRepository, times(1)).deleteById(productId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product delete successfully", response.getBody());
    }

    @Test
    public void testUpdateProduct() {
        Long productId = 1L;
        Product updatedProduct = new Product(1L, "SKU001", "Product1", "Description1", 100.0, true);
        Product product = new Product(1L, "SKU002", "Product2", "Description2", 200.0, false);

        when(productRepository.findById(productId)).thenReturn(java.util.Optional.of(product));

        productService.UpdateProduct(productId, updatedProduct);

        verify(productRepository, times(1)).save(product);
        assertEquals("SKU001", product.getSku());
        assertEquals("Product1", product.getName());
        assertEquals("Description1", product.getDescription());
        assertEquals(100.0, product.getPrice());
        assertEquals(true, product.getStatus());
    }

    @Test
    public void testNewProduct() {
        Product product = new Product(1L, "SKU001", "Product1", "Description1", 100.0, true);

        ResponseEntity<Object> response = productService.newProduct(product);

        verify(productRepository, times(1)).save(product);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

}