package com.ecommerce.service;

import com.ecommerce.model.*;
import java.util.*;

public class RatingService {
    private static final RatingService INSTANCE = new RatingService();
    private final Map<Integer, Set<Integer>> ratedProducts = new HashMap<>();
    private RatingService() {}
    public static RatingService getInstance() { return INSTANCE; }

    public synchronized boolean rate(Customer customer, Product product, int stars) {
        if (customer == null || product == null || stars < 1 || stars > 5) return false;
        Set<Integer> ids = ratedProducts.computeIfAbsent(customer.getUserId(), k -> new HashSet<>());
        if (!ids.add(product.getProductId())) return false;
        product.addRating(stars);
        return true;
    }

    public synchronized boolean hasRated(Customer customer, Product product) {
        return customer != null && product != null
                && ratedProducts.getOrDefault(customer.getUserId(), Set.of())
                .contains(product.getProductId());
    }
}
