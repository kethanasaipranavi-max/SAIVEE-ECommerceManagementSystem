package com.ecommerce.service;

import com.ecommerce.model.*;
import java.util.*;

public class RecommendationService {
    private final ProductService productService;
    private final OrderService orderService;

    public RecommendationService(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    public List<Product> recommend(Customer customer, Product current, int limit) {
        List<Product> products = productService == null ? List.of() : productService.getProducts();
        if (products.isEmpty()) return new ArrayList<>();

        Map<String,Integer> cat = new HashMap<>();
        Map<String,Integer> sub = new HashMap<>();
        Map<String,Integer> brand = new HashMap<>();
        Set<Integer> purchased = new HashSet<>();

        if (customer != null && orderService != null) {
            for (Order order : orderService.getCustomerOrders(customer)) {
                for (OrderItem item : order.getOrderItems()) {
                    if (item == null || item.getProduct() == null) continue;
                    Product p = item.getProduct();
                    purchased.add(p.getProductId());
                    cat.merge(p.getCategory(), 1, Integer::sum);
                    sub.merge(p.getSubcategory(), 1, Integer::sum);
                    brand.merge(p.getBrand(), 1, Integer::sum);
                }
            }
        }

        List<ProductScore> scored = new ArrayList<>();
        for (Product p : products) {
            if (p == null || (current != null && p.getProductId() == current.getProductId())) continue;
            int score = 0;
            if (current != null) {
                if (same(p.getCategory(), current.getCategory())) score += 35;
                if (same(p.getSubcategory(), current.getSubcategory())) score += 45;
                if (same(p.getBrand(), current.getBrand())) score += 25;
            }
            score += cat.getOrDefault(p.getCategory(), 0) * 8;
            score += sub.getOrDefault(p.getSubcategory(), 0) * 12;
            score += brand.getOrDefault(p.getBrand(), 0) * 6;
            score += (int)Math.round(p.getAverageRating() * 5);
            if (purchased.contains(p.getProductId())) score -= 20;
            if (p.getQuantity() <= 0) score -= 100;
            scored.add(new ProductScore(p, score));
        }
        scored.sort(Comparator.comparingInt(ProductScore::score).reversed());
        List<Product> result = new ArrayList<>();
        for (ProductScore x : scored) {
            result.add(x.product());
            if (result.size() >= Math.max(1, limit)) break;
        }
        return result;
    }

    private boolean same(String a, String b) {
        return a != null && b != null && a.equalsIgnoreCase(b);
    }

    private record ProductScore(Product product, int score) {}
}
