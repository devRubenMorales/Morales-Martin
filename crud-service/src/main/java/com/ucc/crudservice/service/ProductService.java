package com.ucc.crudservice.service;


import com.ucc.crudservice.model.entities.Product;
import com.ucc.crudservice.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class ProductService {

    private final ProductRepository productRepository;


    public List<Product> getProducts() {
        return this.productRepository.findAll();
    }

    public ResponseEntity<Object> newProduct(Product product) {
        HashMap<String, Object> data = new HashMap<>();
        productRepository.save(product);
        data.put("data", product);
        data.put("message","Successfully saved");
        return new ResponseEntity<>(
                data,
                HttpStatus.CREATED
        );
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public ResponseEntity<Object> deleteProduct(Long id) {
        productRepository.deleteById(id);
        return new ResponseEntity<>("Product delete successfully", HttpStatus.OK);
    }

    public void UpdateProduct(Long productId, Product updatedProduct) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setSku(updatedProduct.getSku());
            product.setName(updatedProduct.getName());
            product.setDescription(updatedProduct.getDescription());
            product.setPrice(updatedProduct.getPrice());
            product.setStatus(updatedProduct.getStatus());
            productRepository.save(product);
        } else {
            throw new RuntimeException("Product not found");
        }
    }
}