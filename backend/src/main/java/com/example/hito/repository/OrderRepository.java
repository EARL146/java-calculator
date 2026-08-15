package com.example.hito.repository;

import com.example.hito.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * OrderRepository — database access for Order entities.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Find orders by their status (e.g., "PENDING", "CONFIRMED").
     * Generated SQL: SELECT * FROM orders WHERE status = ?
     */
    List<Order> findByStatus(String status);

    /**
     * Find all orders sorted by newest first.
     * Generated SQL: SELECT * FROM orders ORDER BY created_at DESC
     */
    List<Order> findAllByOrderByCreatedAtDesc();
}
