package com.example.ecommerce.model;

import java.math.BigDecimal;

public class CartItem {

    private String productId;
    private String productName;
    private String image;
    private BigDecimal price;
    private int quantity;

    public CartItem() { }

    public CartItem(String productId, String productName, String image, BigDecimal price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
