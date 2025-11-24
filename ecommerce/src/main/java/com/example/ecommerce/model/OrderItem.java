package com.example.ecommerce.model;

import java.math.BigDecimal;

public class OrderItem {

    private String productId;
    private String productName;
    private int quantity;
    private BigDecimal pricePerUnit;
    private BigDecimal subtotal;

    public OrderItem() {}

    public OrderItem(String productId, String productName, int quantity,
                     BigDecimal pricePerUnit) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.subtotal = pricePerUnit.multiply(BigDecimal.valueOf(quantity));
    }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getPricePerUnit() { return pricePerUnit; }
    public void setPricePerUnit(BigDecimal pricePerUnit) { this.pricePerUnit = pricePerUnit; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}
