package com.example.hito.controller;

import com.example.hito.entity.Product;
import com.example.hito.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * ProductController — exposes REST API endpoints for Products.
 *
 * @RestController = @Controller + @ResponseBody
 * It means every method returns JSON automatically (no HTML templates).
 *
 * @RequestMapping("/api/products") means all endpoints here start with /api/products.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * GET /api/products
     * Returns all products. Optionally filter by ?category=Fried+Hito
     *
     * Example frontend call:
     * fetch("http://localhost:8080/api/products")
     */
    @GetMapping
    public ResponseEntity<List<Product>> getProducts(
            @RequestParam(required = false) String category) {

        List<Product> products;

        if (category != null && !category.isBlank()) {
            products = productService.getProductsByCategory(category);
        } else {
            products = productService.getAvailableProducts();
        }

        return ResponseEntity.ok(products);
    }

    /**
     * GET /api/products/{id}
     * Returns a single product by its ID.
     *
     * Example: GET /api/products/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);

        // If found → return 200 OK with the product
        // If not found → return 404 Not Found
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/products
     * Creates a new product. Used by the admin to add products.
     *
     * Body (JSON): { "name": "...", "price": 150, "category": "..." }
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product saved = productService.saveProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * PUT /api/products/{id}
     * Updates an existing product.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product product) {

        Product updated = productService.updateProduct(id, product);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/products/{id}
     * Deletes a product by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteProduct(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
