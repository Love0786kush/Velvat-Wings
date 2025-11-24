package com.example.ecommerce.controller;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) { this.orderService = orderService; }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestParam String email,
                                         @RequestParam String shippingAddress,
                                         @RequestParam(defaultValue = "COD") String paymentMode) {
        try {
            Order order = orderService.createOrderFromCart(email, shippingAddress, paymentMode);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Order failed ❌ " + e.getMessage());
        }
    }

    @GetMapping("/my")
    public ResponseEntity<?> myOrders(@RequestParam String email) {
        try {
            List<Order> orders = orderService.getOrdersForUser(email);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
