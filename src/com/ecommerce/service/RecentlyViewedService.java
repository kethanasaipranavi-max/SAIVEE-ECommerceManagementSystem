package com.ecommerce.service;

import com.ecommerce.model.Customer;
import com.ecommerce.model.Product;
import java.util.*;

public class RecentlyViewedService {
    private static final RecentlyViewedService INSTANCE = new RecentlyViewedService();
    private static final int MAX_ITEMS = 10;
    private final Map<Integer, LinkedHashMap<Integer, Product>> viewed = new HashMap<>();
    private RecentlyViewedService() {}
    public static RecentlyViewedService getInstance() { return INSTANCE; }

    public synchronized void record(Customer customer, Product product) {
        if (customer == null || product == null) return;
        LinkedHashMap<Integer, Product> list = viewed.computeIfAbsent(
                customer.getUserId(), k -> new LinkedHashMap<>(16, .75f, true));
        list.put(product.getProductId(), product);
        while (list.size() > MAX_ITEMS) list.remove(list.keySet().iterator().next());
    }

    public synchronized List<Product> getRecentlyViewed(Customer customer) {
        if (customer == null) return Collections.emptyList();
        LinkedHashMap<Integer, Product> list = viewed.get(customer.getUserId());
        if (list == null) return Collections.emptyList();
        List<Product> result = new ArrayList<>(list.values());
        Collections.reverse(result);
        return Collections.unmodifiableList(result);
    }

    public synchronized void clear(Customer customer) {
        if (customer != null) viewed.remove(customer.getUserId());
    }
}
