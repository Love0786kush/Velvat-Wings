package com.example.ecommerce.service;

import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    public OrderService(UserRepository userRepository,
                        CartRepository cartRepository,
                        OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
    }

    public Order createOrderFromCart(String email, String shippingAddress, String paymentMode) throws Exception {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("User not found"));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new Exception("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new Exception("Cart is empty");
        }

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(i -> new OrderItem(i.getProductId(), i.getProductName(),
                        i.getQuantity(), i.getPrice()))
                .collect(Collectors.toList());

        BigDecimal total = orderItems.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUserId(user.getId());
        order.setItems(orderItems);
        order.setTotalAmount(total);
        order.setPaymentMode(paymentMode);
        order.setPaymentStatus("PENDING");
        order.setOrderStatus("PLACED");
        order.setShippingAddress(shippingAddress);
        order.setCreatedAt(Instant.now());
        order.setUpdatedAt(Instant.now());

        Order saved = orderRepository.save(order);

        // clear cart
        cart.getItems().clear();
        cart.recalculateTotal();
        cartRepository.save(cart);

        return saved;
    }

    public List<Order> getOrdersForUser(String email) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("User not found"));
        return orderRepository.findByUserId(user.getId());
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateStatus(String orderId, String status) throws Exception {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Order not found"));

        order.setOrderStatus(status);
        order.setUpdatedAt(Instant.now());
        return orderRepository.save(order);
    }
}
