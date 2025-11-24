package com.example.ecommerce.controller;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@CrossOrigin(origins = "*")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) { this.orderService = orderService; }

    @GetMapping("/all")
    public ResponseEntity<List<Order>> all() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateStatus(@PathVariable String orderId,
                                          @RequestParam String status) {
        try {
            Order order = orderService.updateStatus(orderId, status);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
