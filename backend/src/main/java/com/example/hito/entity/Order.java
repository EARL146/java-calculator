package com.example.hito.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Order Entity — maps to the "orders" table.
 *
 * One Order can have many OrderItems (one-to-many relationship).
 * We use @OneToMany and @JoinColumn to express this in JPA.
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "contact_number", nullable = false)
    private String contactNumber;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(columnDefinition = "TEXT")
    private String notes;

    /**
     * Status can be: PENDING, CONFIRMED, PREPARING, READY, DELIVERED, CANCELLED
     */
    @Column(nullable = false)
    private String status = "PENDING";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * @OneToMany: one order has many items.
     * cascade = ALL: when we save an order, its items are also saved automatically.
     * orphanRemoval = true: if an item is removed from the list, it's deleted from the DB.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items = new ArrayList<>();

    // ----------------------------------------------------------------
    // Lifecycle callback — runs before saving to DB
    // ----------------------------------------------------------------

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // ----------------------------------------------------------------
    // Constructors
    // ----------------------------------------------------------------

    public Order() {}

    // ----------------------------------------------------------------
    // Getters and Setters
    // ----------------------------------------------------------------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}
