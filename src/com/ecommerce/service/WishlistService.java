package com.ecommerce.service;

import com.ecommerce.model.Customer;
import com.ecommerce.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WishlistService {

    private static final WishlistService INSTANCE = new WishlistService();
    public static WishlistService getInstance() { return INSTANCE; }

    private final Map<Integer, List<Product>> wishlists;

    public WishlistService() {
        wishlists = new HashMap<>();
    }

    // =========================================================
    // ADD TO WISHLIST
    // =========================================================

    public synchronized void addToWishlist(
            Customer customer,
            Product product) {

        if (customer == null
                || product == null) {

            return;
        }

        List<Product> wishlist =
                getOrCreateWishlist(customer);

        if (containsProduct(
                customer,
                product)) {

            System.out.println(
                    product.getProductName()
                            + " is already in the wishlist."
            );

            return;
        }

        wishlist.add(product);

        System.out.println(
                product.getProductName()
                        + " added to wishlist."
        );
    }

    // =========================================================
    // REMOVE FROM WISHLIST
    // =========================================================

    public synchronized void removeFromWishlist(
            Customer customer,
            Product product) {

        if (customer == null
                || product == null) {

            return;
        }

        List<Product> wishlist =
                getOrCreateWishlist(customer);

        boolean removed =
                wishlist.removeIf(
                        item ->
                                item != null
                                        && item.getProductId()
                                        == product.getProductId()
                );

        if (removed) {

            System.out.println(
                    product.getProductName()
                            + " removed from wishlist."
            );
        }
    }

    // =========================================================
    // CHECK PRODUCT
    // =========================================================

    public synchronized boolean containsProduct(
            Customer customer,
            Product product) {

        if (customer == null
                || product == null) {

            return false;
        }

        List<Product> wishlist =
                getOrCreateWishlist(customer);

        for (Product item :
                wishlist) {

            if (item != null
                    && item.getProductId()
                    == product.getProductId()) {

                return true;
            }
        }

        return false;
    }

    // =========================================================
    // GET WISHLIST
    //
    // Returns a read-only copy.
    // =========================================================

    public synchronized List<Product> getWishlist(
            Customer customer) {

        if (customer == null) {

            return Collections.emptyList();
        }

        List<Product> wishlist =
                getOrCreateWishlist(customer);

        return Collections.unmodifiableList(
                new ArrayList<>(
                        wishlist
                )
        );
    }

    // =========================================================
    // INTERNAL WISHLIST
    // =========================================================

    private List<Product> getOrCreateWishlist(
            Customer customer) {

        return wishlists.computeIfAbsent(
                customer.getUserId(),
                id -> new ArrayList<>()
        );
    }

    // =========================================================
    // WISHLIST COUNT
    // =========================================================

    public synchronized int getWishlistCount(
            Customer customer) {

        if (customer == null) {
            return 0;
        }

        return getOrCreateWishlist(
                customer
        ).size();
    }

    // =========================================================
    // CLEAR WISHLIST
    // =========================================================

    public synchronized void clearWishlist(
            Customer customer) {

        if (customer == null) {
            return;
        }

        List<Product> wishlist =
                getOrCreateWishlist(customer);

        wishlist.clear();

        System.out.println(
                "Wishlist cleared successfully."
        );
    }

    // =========================================================
    // DISPLAY WISHLIST
    // =========================================================

    public synchronized void displayWishlist(
            Customer customer) {

        if (customer == null) {

            System.out.println(
                    "Customer not found."
            );

            return;
        }

        List<Product> wishlist =
                getOrCreateWishlist(customer);

        if (wishlist.isEmpty()) {

            System.out.println(
                    "Your wishlist is empty."
            );

            return;
        }

        System.out.println(
                "===== SAIVEE WISHLIST ====="
        );

        System.out.println(
                "Customer: "
                        + customer.getName()
        );

        System.out.println(
                "Total Products: "
                        + wishlist.size()
        );

        for (Product product :
                wishlist) {

            if (product == null) {
                continue;
            }

            System.out.println(
                    "----------------------------"
            );

            System.out.println(
                    product
            );
        }

        System.out.println(
                "----------------------------"
        );
    }

    // =========================================================
    // REMOVE CUSTOMER WISHLIST
    //
    // Useful when a customer account is deleted.
    // =========================================================

    public synchronized void removeCustomerWishlist(
            int customerId) {

        wishlists.remove(
                customerId
        );
    }

    // =========================================================
    // TOTAL WISHLISTS
    // =========================================================

    public synchronized int getCustomerWishlistCount() {

        return wishlists.size();
    }
}