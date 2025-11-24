package com.example.ecommerce.service;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsService {

    private final OrderRepository orderRepository;

    public StatsService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Map<String, Object> getStatsForDate(LocalDate date) {

        Instant start = date.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        List<Order> orders = orderRepository.findByCreatedAtBetween(start, end);

        int totalOrders = orders.size();
        BigDecimal totalRevenue = orders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> result = new HashMap<>();
        result.put("date", date.toString());
        result.put("totalOrders", totalOrders);
        result.put("totalRevenue", totalRevenue);
        return result;
    }

    public Map<String, Object> getTodayStats() {
        return getStatsForDate(LocalDate.now());
    }
}
