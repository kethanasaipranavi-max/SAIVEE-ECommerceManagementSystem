package com.ecommerce.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private int cartId;
    private Customer customer;
    private List<OrderItem> items;

    public Cart() {
        items = new ArrayList<>();
    }

    public Cart(int cartId, Customer customer) {
        this.cartId = cartId;
        this.customer = customer;
        this.items = new ArrayList<>();
    }

    public int getCartId() {
        return cartId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void addItem(
            Product product,
            int quantity) {

        if (product == null || quantity <= 0) {
            return;
        }

        // If product already exists,
        // increase its quantity.
        for (OrderItem item : items) {

            if (item.getProduct()
                    .getProductId()
                    == product.getProductId()) {

                item.setQuantity(
                        item.getQuantity()
                                + quantity
                );

                return;
            }
        }

        // Otherwise create a new cart item.
        items.add(
                new OrderItem(
                        product,
                        quantity
                )
        );
    }

    public void addItem(Product product, int quantity, String variantKey) {
        if (product == null || quantity <= 0) return;
        for (OrderItem item : items) {
            if (item.getProduct().getProductId() == product.getProductId()
                    && java.util.Objects.equals(item.getVariantKey(), variantKey)) {
                item.setQuantity(item.getQuantity() + quantity); return;
            }
        }
        OrderItem item = new OrderItem(product, quantity);
        item.setVariantKey(variantKey);
        items.add(item);
    }

    public void removeItem(
            Product product) {

        if (product == null) {
            return;
        }

        items.removeIf(
                item ->
                        item.getProduct()
                                .getProductId()
                                == product.getProductId()
        );
    }

    public void updateQuantity(
            Product product,
            int quantity) {

        if (product == null) {
            return;
        }

        for (OrderItem item : items) {

            if (item.getProduct()
                    .getProductId()
                    == product.getProductId()) {

                if (quantity <= 0) {
                    removeItem(product);
                } else {
                    item.setQuantity(quantity);
                }

                return;
            }
        }
    }

    public int getTotalItems() {

        int total = 0;

        for (OrderItem item : items) {
            total += item.getQuantity();
        }

        return total;
    }

    public double calculateTotal() {

        double total = 0.0;

        for (OrderItem item : items) {
            total += item.calculateSubtotal();
        }

        return total;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clearCart() {
        items.clear();
    }

    public void displayCart() {

        if (items.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }

        System.out.println(
                "===== SAIVEE CART ====="
        );

        if (customer != null) {
            System.out.println(
                    "Customer: "
                            + customer.getName()
            );
        }

        System.out.println(
                "Total Items: "
                        + getTotalItems()
        );

        System.out.println();

        for (OrderItem item : items) {

            System.out.println(
                    "----------------------------"
            );

            System.out.println(item);
        }

        System.out.println(
                "----------------------------"
        );

        System.out.println(
                "Cart Total: ₹"
                        + String.format(
                        "%.2f",
                        calculateTotal()
                )
        );
    }
}