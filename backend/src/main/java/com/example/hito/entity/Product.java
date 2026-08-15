package com.example.hito.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Product Entity
 *
 * This class maps to the "products" table in MySQL.
 * Each field = one column in the table.
 *
 * @Entity tells JPA "this class is a database table."
 * @Table(name = "products") sets the exact table name.
 */
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * Category examples: "Fresh Hito", "Fried Hito", "Grilled Hito",
     * "Hito Meals", "Hito Specials"
     */
    @Column(nullable = false)
    private String category;

    /** URL to the product image (can be a local path or external URL) */
    @Column(name = "image_url")
    private String imageUrl;

    /** Whether the product is currently available for order */
    @Column(nullable = false)
    private boolean available = true;

    // ----------------------------------------------------------------
    // Constructors
    // ----------------------------------------------------------------

    public Product() {}

    public Product(String name, String description, BigDecimal price,
                   String category, String imageUrl, boolean available) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
        this.available = available;
    }

    // ----------------------------------------------------------------
    // Getters and Setters
    // Spring Data JPA needs these to read and write data.
    // ----------------------------------------------------------------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
