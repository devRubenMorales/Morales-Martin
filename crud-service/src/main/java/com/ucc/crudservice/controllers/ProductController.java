package com.ucc.crudservice.controllers;


import com.ucc.crudservice.model.entities.Product;
import com.ucc.crudservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getAllProducts() {
        return this.productService.getProducts();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void newProduct(@RequestBody Product product){
        this.productService.newProduct(product);
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Product getProductById(@PathVariable Long id){
        return this.productService.getProductById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProduct(@PathVariable Long id){
        this.productService.deleteProduct(id);
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct){
        this.productService.UpdateProduct(id, updatedProduct);
    }
}