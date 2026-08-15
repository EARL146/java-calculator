package com.example.hito.service;

import com.example.hito.dto.OrderRequest;
import com.example.hito.entity.Order;
import com.example.hito.entity.OrderItem;
import com.example.hito.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * OrderService — handles order creation and retrieval logic.
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    /** Get all orders (newest first) */
    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    /** Get a single order by ID */
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    /**
     * Create a new order from the frontend's OrderRequest DTO.
     *
     * We convert the DTO → Entity here in the Service layer.
     * This keeps conversion logic out of the Controller.
     */
    public Order createOrder(OrderRequest request) {
        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setContactNumber(request.getContactNumber());
        order.setAddress(request.getAddress());
        order.setNotes(request.getNotes());
        order.setTotalAmount(request.getTotalAmount());
        order.setStatus("PENDING");

        // Convert each OrderItemRequest → OrderItem entity
        List<OrderItem> items = request.getItems().stream()
                .map(itemReq -> new OrderItem(
                        itemReq.getProductId(),
                        itemReq.getProductName(),
                        itemReq.getQuantity(),
                        itemReq.getPrice()
                ))
                .toList();

        order.setItems(items);

        // Save to DB — cascade saves items too
        return orderRepository.save(order);
    }

    /** Update the status of an order (e.g., PENDING → CONFIRMED) */
    public Order updateOrderStatus(Long id, String status) {
        Optional<Order> optional = orderRepository.findById(id);
        if (optional.isEmpty()) return null;

        Order order = optional.get();
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
