package com.example.ecommerce.controller;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // email query param (frontend se)
    @GetMapping
    public ResponseEntity<?> getCart(@RequestParam String email) {
        try {
            Cart cart = cartService.getCartByEmail(email);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addItem(@RequestParam String email,
                                     @RequestParam String productId,
                                     @RequestParam int quantity) {
        try {
            Cart cart = cartService.addItem(email, productId, quantity);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateItem(@RequestParam String email,
                                        @RequestParam String productId,
                                        @RequestParam int quantity) {
        try {
            Cart cart = cartService.updateItem(email, productId, quantity);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clear(@RequestParam String email) {
        try {
            cartService.clearCart(email);
            return ResponseEntity.ok("Cart cleared");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
