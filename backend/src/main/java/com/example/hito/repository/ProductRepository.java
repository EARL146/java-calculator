package com.example.hito.repository;

import com.example.hito.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ProductRepository
 *
 * HOW THIS WORKS:
 * By extending JpaRepository, Spring Data JPA automatically gives us:
 *   - findAll()         → SELECT * FROM products
 *   - findById(id)      → SELECT * FROM products WHERE id = ?
 *   - save(product)     → INSERT or UPDATE
 *   - deleteById(id)    → DELETE FROM products WHERE id = ?
 *   - count()           → SELECT COUNT(*) FROM products
 *
 * We don't need to write SQL for these — Spring generates them automatically.
 *
 * We can also define custom queries by method naming convention (see below).
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Spring reads "findByCategory" and automatically generates:
     * SELECT * FROM products WHERE category = ?
     */
    List<Product> findByCategory(String category);

    /**
     * Finds only products that are currently available.
     * SELECT * FROM products WHERE available = true
     */
    List<Product> findByAvailableTrue();

    /**
     * Finds available products in a specific category.
     */
    List<Product> findByCategoryAndAvailableTrue(String category);
}
