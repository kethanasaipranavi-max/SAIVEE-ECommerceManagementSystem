package com.ecommerce.model;

import com.ecommerce.exception.InsufficientStockException;

public class Inventory {

    private final Product product;
    private int stock;
    private int reservedStock;

    public Inventory() {
        this.product = null;
        this.stock = 0;
        this.reservedStock = 0;
    }

    public Inventory(Product product, int stock) {

        if (stock < 0) {
            throw new IllegalArgumentException(
                    "Stock cannot be negative."
            );
        }

        this.product = product;
        this.stock = stock;
        this.reservedStock = 0;
    }

    public Product getProduct() {
        return product;
    }

    public int getStock() {
        return stock;
    }

    public int getReservedStock() {
        return reservedStock;
    }

    public int getAvailableStock() {
        return stock - reservedStock;
    }

    public void addStock(int quantity) {

        if (quantity < 0) {
            throw new IllegalArgumentException(
                    "Quantity cannot be negative."
            );
        }

        stock += quantity;

        System.out.println(
                "Stock added successfully."
        );
    }

    public void removeStock(int quantity)
            throws InsufficientStockException {

        if (quantity < 0) {
            throw new IllegalArgumentException(
                    "Quantity cannot be negative."
            );
        }

        if (quantity > getAvailableStock()) {
            throw new InsufficientStockException(
                    "Insufficient available stock."
            );
        }

        stock -= quantity;

        System.out.println(
                "Stock removed successfully."
        );
    }

    public void reserveStock(int quantity)
            throws InsufficientStockException {

        if (quantity < 0) {
            throw new IllegalArgumentException(
                    "Quantity cannot be negative."
            );
        }

        if (quantity > getAvailableStock()) {
            throw new InsufficientStockException(
                    "Insufficient stock for reservation."
            );
        }

        reservedStock += quantity;

        System.out.println(
                "Stock reserved successfully."
        );
    }

    public void releaseReservedStock(int quantity) {

        if (quantity < 0) {
            throw new IllegalArgumentException(
                    "Quantity cannot be negative."
            );
        }

        if (quantity > reservedStock) {
            quantity = reservedStock;
        }

        reservedStock -= quantity;

        System.out.println(
                "Reserved stock released successfully."
        );
    }

    public void displayInventory() {

        System.out.println(
                "----------------------------"
        );

        System.out.println(
                "Product: "
                        + (product == null
                        ? "None"
                        : product.getProductName())
        );

        System.out.println(
                "Stock: " + stock
        );

        System.out.println(
                "Reserved Stock: " + reservedStock
        );

        System.out.println(
                "Available Stock: "
                        + getAvailableStock()
        );
    }

    @Override
    public String toString() {

        return "Inventory{" +
                "product=" + product +
                ", stock=" + stock +
                ", reservedStock=" + reservedStock +
                ", availableStock=" + getAvailableStock() +
                '}';
    }
}