package com.ecommerce.service;

import com.ecommerce.model.Cart;
import com.ecommerce.model.Customer;
import com.ecommerce.model.OrderItem;
import com.ecommerce.model.Product;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CartService {

    private final Map<Integer, Cart> customerCarts;

    public CartService() {
        customerCarts = new HashMap<>();
    }

    // =========================================================
    // GET CUSTOMER CART
    // =========================================================

    public synchronized Cart getCart(
            Customer customer) {

        if (customer == null) {
            return null;
        }

        return customerCarts.computeIfAbsent(
                customer.getUserId(),
                id -> new Cart(
                        id,
                        customer
                )
        );
    }

    // =========================================================
    // ADD TO CART
    // =========================================================

    public synchronized boolean addToCart(
            Customer customer,
            Product product,
            int quantity) {

        if (customer == null) {

            System.out.println(
                    "Customer not found."
            );

            return false;
        }

        if (product == null) {

            System.out.println(
                    "Product not found."
            );

            return false;
        }

        if (quantity <= 0) {

            System.out.println(
                    "Quantity must be greater than zero."
            );

            return false;
        }

        if (product.getQuantity() <= 0) {

            System.out.println(
                    "Product is out of stock."
            );

            return false;
        }

        Cart cart =
                getCart(customer);

        if (cart == null) {
            return false;
        }

        int existingQuantity = 0;

        for (OrderItem item :
                cart.getItems()) {

            if (item != null
                    && item.getProduct() != null
                    && item.getProduct()
                    .getProductId()
                    == product.getProductId()) {

                existingQuantity =
                        item.getQuantity();

                break;
            }
        }

        int newQuantity =
                existingQuantity + quantity;

        if (newQuantity > product.getQuantity()) {

            System.out.println(
                    "Cannot add product. "
                            + "Requested quantity exceeds stock."
            );

            return false;
        }

        cart.addItem(
                product,
                quantity
        );

        System.out.println(
                "Product added to cart successfully."
        );

        return true;
    }

    public synchronized boolean addToCart(Customer customer, Product product, int quantity, String variantKey) {
        if (variantKey == null || variantKey.isBlank()) return addToCart(customer, product, quantity);
        if (customer == null || product == null || quantity <= 0) return false;
        int available = product.getVariantStock(variantKey);
        if (available < quantity) return false;
        Cart cart = getCart(customer);
        int existing = 0;
        for (OrderItem item : cart.getItems()) if (item.getProduct().getProductId() == product.getProductId() && java.util.Objects.equals(item.getVariantKey(), variantKey)) existing = item.getQuantity();
        if (existing + quantity > available) return false;
        cart.addItem(product, quantity, variantKey);
        return true;
    }

    // =========================================================
    // REMOVE FROM CART
    // =========================================================

    public synchronized boolean removeFromCart(
            Customer customer,
            Product product) {

        if (customer == null
                || product == null) {

            return false;
        }

        Cart cart =
                getCart(customer);

        if (cart == null) {
            return false;
        }

        boolean found = false;

        for (OrderItem item :
                cart.getItems()) {

            if (item != null
                    && item.getProduct() != null
                    && item.getProduct()
                    .getProductId()
                    == product.getProductId()) {

                found = true;
                break;
            }
        }

        if (!found) {

            System.out.println(
                    "Product is not present in cart."
            );

            return false;
        }

        cart.removeItem(product);

        System.out.println(
                "Product removed from cart."
        );

        return true;
    }

    // =========================================================
    // UPDATE QUANTITY
    // =========================================================

    public synchronized boolean updateQuantity(
            Customer customer,
            Product product,
            int quantity) {

        if (customer == null
                || product == null) {

            return false;
        }

        Cart cart =
                getCart(customer);

        if (cart == null) {
            return false;
        }

        if (quantity <= 0) {

            return removeFromCart(
                    customer,
                    product
            );
        }

        if (quantity > product.getQuantity()) {

            System.out.println(
                    "Requested quantity exceeds stock."
            );

            return false;
        }

        boolean productExists = false;

        for (OrderItem item :
                cart.getItems()) {

            if (item != null
                    && item.getProduct() != null
                    && item.getProduct()
                    .getProductId()
                    == product.getProductId()) {

                productExists = true;
                break;
            }
        }

        if (!productExists) {

            System.out.println(
                    "Product is not present in cart."
            );

            return false;
        }

        cart.updateQuantity(
                product,
                quantity
        );

        System.out.println(
                "Cart quantity updated successfully."
        );

        return true;
    }

    // =========================================================
    // CLEAR CART
    // =========================================================

    public synchronized boolean clearCart(
            Customer customer) {

        if (customer == null) {
            return false;
        }

        Cart cart =
                getCart(customer);

        if (cart == null) {
            return false;
        }

        if (cart.isEmpty()) {

            System.out.println(
                    "Cart is already empty."
            );

            return true;
        }

        cart.clearCart();

        System.out.println(
                "Cart cleared successfully."
        );

        return true;
    }

    // =========================================================
    // GET CART TOTAL
    // =========================================================

    public synchronized double getCartTotal(
            Customer customer) {

        Cart cart =
                getCart(customer);

        if (cart == null) {
            return 0.0;
        }

        return cart.calculateTotal();
    }

    // =========================================================
    // GET TOTAL ITEMS
    // =========================================================

    public synchronized int getTotalItems(
            Customer customer) {

        Cart cart =
                getCart(customer);

        if (cart == null) {
            return 0;
        }

        return cart.getTotalItems();
    }

    // =========================================================
    // CHECK EMPTY
    // =========================================================

    public synchronized boolean isCartEmpty(
            Customer customer) {

        Cart cart =
                getCart(customer);

        return cart == null
                || cart.isEmpty();
    }

    // =========================================================
    // DISPLAY CART
    // =========================================================

    public synchronized void displayCart(
            Customer customer) {

        Cart cart =
                getCart(customer);

        if (cart != null) {

            cart.displayCart();

        } else {

            System.out.println(
                    "Cart not found."
            );
        }
    }

    // =========================================================
    // FIND CART BY ID
    // =========================================================

    /*
     * Cart ID is the customer's ID.
     * Used by OrderProcessingService.
     */
    public synchronized Cart findCartById(
            int cartId) {

        return customerCarts.get(cartId);
    }

    // =========================================================
    // CART EXISTS
    // =========================================================

    public synchronized boolean cartExists(
            int cartId) {

        return customerCarts.containsKey(
                cartId
        );
    }

    // =========================================================
    // GET CART COUNT
    // =========================================================

    public synchronized int getCartCount() {

        return customerCarts.size();
    }

    // =========================================================
    // GET ALL CARTS
    // =========================================================

    public synchronized Map<Integer, Cart>
    getCustomerCarts() {

        return Collections.unmodifiableMap(
                new HashMap<>(
                        customerCarts
                )
        );
    }

    // =========================================================
    // REMOVE CUSTOMER CART
    // =========================================================

    public synchronized void removeCustomerCart(
            int customerId) {

        customerCarts.remove(
                customerId
        );
    }
}