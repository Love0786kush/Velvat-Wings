package com.example.ecommerce.controller;

import com.example.ecommerce.service.StatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/stats")
@CrossOrigin(origins = "*")
public class AdminStatsController {

    private final StatsService statsService;

    public AdminStatsController(StatsService statsService) { this.statsService = statsService; }

    @GetMapping("/today")
    public ResponseEntity<Map<String, Object>> today() {
        return ResponseEntity.ok(statsService.getTodayStats());
    }

    @GetMapping("/date")
    public ResponseEntity<Map<String, Object>> byDate(@RequestParam String date) {
        LocalDate d = LocalDate.parse(date);
        return ResponseEntity.ok(statsService.getStatsForDate(d));
    }
}
