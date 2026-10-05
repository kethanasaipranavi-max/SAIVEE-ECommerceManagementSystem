package com.ecommerce.model;

public class ProductVariant {
    private final String size;
    private final String color;
    private int stock;

    public ProductVariant(String size, String color, int stock) {
        this.size = size == null ? "" : size.trim();
        this.color = color == null ? "" : color.trim();
        if (stock < 0) throw new IllegalArgumentException("Variant stock cannot be negative.");
        this.stock = stock;
    }

    public String getSize() { return size; }
    public String getColor() { return color; }
    public synchronized int getStock() { return stock; }
    public synchronized void addStock(int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative.");
        stock += quantity;
    }
    public synchronized boolean removeStock(int quantity) {
        if (quantity <= 0 || quantity > stock) return false;
        stock -= quantity;
        return true;
    }
    public String getKey() { return size + "|" + color; }
    @Override public String toString() {
        return (size.isBlank() ? "One Size" : "Size: " + size)
                + (color.isBlank() ? "" : ", Color: " + color)
                + " (Stock: " + getStock() + ")";
    }
}
