package com.example.hito.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

/**
 * OrderRequest DTO (Data Transfer Object)
 *
 * WHY A DTO?
 * Instead of the frontend sending a raw Order entity, we use a DTO
 * to control exactly what the frontend is allowed to send.
 * This keeps the API clean and secure.
 *
 * The frontend sends JSON like this:
 * {
 *   "customerName": "Juan Dela Cruz",
 *   "contactNumber": "09123456789",
 *   "address": "123 Brgy. Sample, Cebu City",
 *   "notes": "Extra sawsawan please",
 *   "items": [
 *     { "productId": 1, "productName": "Fried Hito", "quantity": 2, "price": 150.00 }
 *   ]
 * }
 */
public class OrderRequest {

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Contact number is required")
    private String contactNumber;

    @NotBlank(message = "Address is required")
    private String address;

    private String notes;

    @NotNull(message = "Total amount is required")
    @Positive(message = "Total amount must be greater than zero")
    private BigDecimal totalAmount;

    @NotEmpty(message = "Order must have at least one item")
    private List<OrderItemRequest> items;

    // ----------------------------------------------------------------
    // Getters and Setters
    // ----------------------------------------------------------------

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public List<OrderItemRequest> getItems() { return items; }
    public void setItems(List<OrderItemRequest> items) { this.items = items; }

    // ----------------------------------------------------------------
    // Nested DTO: represents a single item inside the order
    // ----------------------------------------------------------------

    public static class OrderItemRequest {

        @NotNull(message = "Product ID is required")
        private Long productId;

        @NotBlank(message = "Product name is required")
        private String productName;

        @Positive(message = "Quantity must be at least 1")
        private int quantity;

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be greater than zero")
        private BigDecimal price;

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
    }
}
