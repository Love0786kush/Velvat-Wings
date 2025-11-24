package com.example.ecommerce.service;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository,
                       ProductRepository productRepository,
                       UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Cart getCartByEmail(String email) throws Exception {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("User not found"));

        Optional<Cart> existing = cartRepository.findByUserId(user.getId());
        return existing.orElseGet(() -> {
            Cart c = new Cart(user.getId());
            return cartRepository.save(c);
        });
    }

    public Cart addItem(String email, String productId, int quantity) throws Exception {

        Cart cart = getCartByEmail(email);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new Exception("Product not found"));

        CartItem existing = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
        } else {
            CartItem item = new CartItem(
                    product.getId(),
                    product.getName(),
                    (product.getImages() != null && !product.getImages().isEmpty())
                            ? product.getImages().get(0) : null,
                    product.getDiscountPrice() != null ? product.getDiscountPrice() : product.getPrice(),
                    quantity
            );
            cart.getItems().add(item);
        }

        cart.recalculateTotal();
        return cartRepository.save(cart);
    }

    public Cart updateItem(String email, String productId, int quantity) throws Exception {
        Cart cart = getCartByEmail(email);

        cart.getItems().removeIf(i -> i.getQuantity() <= 0);

        for (CartItem item : cart.getItems()) {
            if (item.getProductId().equals(productId)) {
                if (quantity <= 0) {
                    cart.getItems().remove(item);
                } else {
                    item.setQuantity(quantity);
                }
                break;
            }
        }

        cart.recalculateTotal();
        return cartRepository.save(cart);
    }

    public void clearCart(String email) throws Exception {
        Cart cart = getCartByEmail(email);
        cart.getItems().clear();
        cart.recalculateTotal();
        cartRepository.save(cart);
    }
}
