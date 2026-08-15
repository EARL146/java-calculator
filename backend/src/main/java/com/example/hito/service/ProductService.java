package com.example.hito.service;

import com.example.hito.entity.Product;
import com.example.hito.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * ProductService — contains the business logic for Products.
 *
 * WHY A SERVICE LAYER?
 * The Controller's job is only to handle HTTP requests/responses.
 * The Service's job is to handle the actual logic (rules, calculations, etc.).
 * The Repository's job is only to talk to the database.
 * Keeping them separate makes the code clean and easy to maintain.
 *
 * @Service tells Spring to manage this class and allow @Autowired injection.
 */
@Service
public class ProductService {

    /**
     * @Autowired tells Spring to automatically provide a ProductRepository instance.
     * We don't create it manually — Spring handles it.
     */
    @Autowired
    private ProductRepository productRepository;

    /** Get all products */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /** Get all currently available products */
    public List<Product> getAvailableProducts() {
        return productRepository.findByAvailableTrue();
    }

    /** Get a product by its ID — returns Optional so we can handle "not found" */
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    /** Get products by category */
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    /**
     * Save a new product OR update an existing one.
     * JPA's save() figures out whether to INSERT or UPDATE based on the ID.
     */
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    /** Update an existing product. Returns null if the product doesn't exist. */
    public Product updateProduct(Long id, Product updatedProduct) {
        Optional<Product> existing = productRepository.findById(id);
        if (existing.isEmpty()) {
            return null;
        }

        Product product = existing.get();
        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());
        product.setCategory(updatedProduct.getCategory());
        product.setImageUrl(updatedProduct.getImageUrl());
        product.setAvailable(updatedProduct.isAvailable());

        return productRepository.save(product);
    }

    /** Delete a product by ID */
    public boolean deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            return false;
        }
        productRepository.deleteById(id);
        return true;
    }
}
