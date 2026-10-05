package com.ecommerce.service;

import com.ecommerce.model.*;
import com.ecommerce.util.AppConstants;
import java.time.*;
import java.util.*;

public class AnalyticsService {
    private final OrderService orderService;
    private final UserService userService;
    private final ProductService productService;

    public AnalyticsService(OrderService orderService, UserService userService, ProductService productService) {
        this.orderService = orderService;
        this.userService = userService;
        this.productService = productService;
    }

    public synchronized double totalSales() {
        return orderService.getOrders().stream().filter(this::isSalesOrder).mapToDouble(Order::getTotalAmount).sum();
    }
    public synchronized long totalOrders() { return orderService.getOrderCount(); }
    public synchronized long totalCustomers() { return userService.getUsers().stream().filter(u -> u instanceof Customer).count(); }
    public synchronized long cancelledOrders() { return orderService.getOrders().stream().filter(o -> o.getStatus() == OrderStatus.CANCELLED).count(); }
    public synchronized long returnedOrders() { return orderService.getOrders().stream().filter(o -> o.getStatus() == OrderStatus.RETURNED || o.getStatus() == OrderStatus.REFUNDED).count(); }
    public synchronized long lowStock() { return productService.getProducts().stream().filter(p -> p.getQuantity() <= AppConstants.LOW_STOCK_THRESHOLD).count(); }

    public synchronized Map<String, Double> salesByPeriod() {
        Map<String, Double> result = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();
        result.put("Daily", salesBetween(today.atStartOfDay(), today.plusDays(1).atStartOfDay()));
        result.put("Weekly", salesBetween(today.minusDays(6).atStartOfDay(), today.plusDays(1).atStartOfDay()));
        result.put("Monthly", salesBetween(today.withDayOfMonth(1).atStartOfDay(), today.plusDays(1).atStartOfDay()));
        return result;
    }

    public synchronized Map<String, Double> salesByCategory() { return salesBy(o -> o.getProduct().getCategory()); }
    public synchronized Map<String, Double> salesByProduct() { return salesBy(o -> o.getProduct().getProductName()); }
    public synchronized Map<String, Double> salesByBrand() { return salesBy(o -> o.getProduct().getBrand()); }

    public synchronized List<Map.Entry<String,Integer>> topProducts(int limit) {
        Map<String,Integer> counts = new HashMap<>();
        for (Order order : orderService.getOrders()) if (isSalesOrder(order)) for (OrderItem item : order.getOrderItems()) counts.merge(item.getProduct().getProductName(), item.getQuantity(), Integer::sum);
        return topEntries(counts, limit);
    }
    public synchronized List<Map.Entry<String,Integer>> topCategories(int limit) {
        Map<String,Integer> counts = new HashMap<>();
        for (Order order : orderService.getOrders()) if (isSalesOrder(order)) for (OrderItem item : order.getOrderItems()) counts.merge(item.getProduct().getCategory(), item.getQuantity(), Integer::sum);
        return topEntries(counts, limit);
    }

    public synchronized double customerSpending(Customer customer) { return customerOrders(customer).stream().filter(this::isSalesOrder).mapToDouble(Order::getTotalAmount).sum(); }
    public synchronized int customerOrderCount(Customer customer) { return customerOrders(customer).size(); }
    public synchronized String favouriteCategory(Customer customer) { return mostPurchased(customer, true); }
    public synchronized String mostPurchasedBrand(Customer customer) { return mostPurchased(customer, false); }
    public synchronized double averageOrderValue(Customer customer) { int n = customerOrderCount(customer); return n == 0 ? 0 : customerSpending(customer) / n; }

    public synchronized String buildAdminReport() {
        StringBuilder s = new StringBuilder();
        s.append("SAIVEE ADMIN ANALYTICS\n=======================\n")
         .append(String.format("Total Sales       : ₹%.2f%n", totalSales()))
         .append("Total Orders      : ").append(totalOrders()).append('\n')
         .append("Total Customers   : ").append(totalCustomers()).append('\n')
         .append("Low Stock         : ").append(lowStock()).append('\n')
         .append("Cancelled Orders  : ").append(cancelledOrders()).append('\n')
         .append("Returned Orders   : ").append(returnedOrders()).append("\n\n");
        s.append("SALES PERIODS\n"); salesByPeriod().forEach((k,v)->s.append(String.format("%-16s: ₹%.2f%n",k,v)));
        appendMap(s, "CATEGORY-WISE SALES", salesByCategory());
        appendMap(s, "PRODUCT-WISE SALES", salesByProduct());
        appendMap(s, "BRAND-WISE SALES", salesByBrand());
        s.append("TOP PRODUCTS\n"); topProducts(5).forEach(e -> s.append("- ").append(e.getKey()).append(": ").append(e.getValue()).append(" units\n"));
        s.append("TOP CATEGORIES\n"); topCategories(5).forEach(e -> s.append("- ").append(e.getKey()).append(": ").append(e.getValue()).append(" units\n"));
        return s.toString();
    }

    private boolean isSalesOrder(Order o) { return o != null && o.getStatus() != OrderStatus.CANCELLED && o.getStatus() != OrderStatus.FAILED; }
    private List<Order> customerOrders(Customer c) { return c == null ? List.of() : orderService.getCustomerOrders(c); }
    private double salesBetween(LocalDateTime from, LocalDateTime to) {
        return orderService.getOrders().stream()
                .filter(this::isSalesOrder)
                .filter(o -> o.getCreatedAt() != null && !o.getCreatedAt().isBefore(from) && o.getCreatedAt().isBefore(to))
                .mapToDouble(Order::getTotalAmount).sum();
    }
    private Map<String,Double> salesBy(java.util.function.Function<OrderItem,String> key) {
        Map<String,Double> m = new TreeMap<>();
        for (Order o : orderService.getOrders()) if (isSalesOrder(o)) for (OrderItem i : o.getOrderItems()) m.merge(key.apply(i), i.calculateSubtotal(), Double::sum);
        return m;
    }
    private String mostPurchased(Customer c, boolean category) {
        Map<String,Integer> m = new HashMap<>();
        for (Order o : customerOrders(c)) if (isSalesOrder(o)) for (OrderItem i : o.getOrderItems()) m.merge(category ? i.getProduct().getCategory() : i.getProduct().getBrand(), i.getQuantity(), Integer::sum);
        return m.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("No data yet");
    }
    private <T> List<Map.Entry<T,Integer>> topEntries(Map<T,Integer> m, int limit) {
        return m.entrySet().stream().sorted(Map.Entry.<T,Integer>comparingByValue().reversed()).limit(limit).toList();
    }
    private void appendMap(StringBuilder s, String title, Map<String,Double> m) {
        s.append(title).append('\n'); if (m.isEmpty()) s.append("- No data yet\n");
        else m.forEach((k,v)->s.append(String.format("- %-28s ₹%.2f%n", k, v)));
        s.append('\n');
    }
}
