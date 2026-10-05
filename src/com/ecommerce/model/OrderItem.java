package com.ecommerce.model;

public class OrderItem {

    private Product product;
    private int quantity;
    private double priceAtPurchase;
    private String variantKey;

    public OrderItem() {
    }

    public OrderItem(Product product, int quantity) {

        this.product = product;
        this.quantity = quantity;
        this.priceAtPurchase = product.getPrice();
        this.variantKey = null;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPriceAtPurchase() {
        return priceAtPurchase;
    }
    public String getVariantKey() { return variantKey; }
    public void setVariantKey(String variantKey) { this.variantKey = variantKey; }

    public double calculateSubtotal() {

        return priceAtPurchase * quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {

        return "Product: " + product.getProductName() +
                "\nQuantity: " + quantity +
                (variantKey == null || variantKey.isBlank() ? "" : "\nVariant: " + variantKey) +
                "\nPrice: ₹" + priceAtPurchase +
                "\nSubtotal: ₹" + calculateSubtotal();
    }
}