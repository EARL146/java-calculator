package com.example.hito.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * OrderItem Entity — maps to the "order_items" table.
 *
 * Each row represents one product line inside an order.
 * Example: 2x Fried Hito at ₱150 each.
 */
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(nullable = false)
    private int quantity;

    /** Price at the time of ordering (in case product price changes later) */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    // ----------------------------------------------------------------
    // Constructors
    // ----------------------------------------------------------------

    public OrderItem() {}

    public OrderItem(Long productId, String productName, int quantity, BigDecimal price) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    // ----------------------------------------------------------------
    // Getters and Setters
    // ----------------------------------------------------------------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
