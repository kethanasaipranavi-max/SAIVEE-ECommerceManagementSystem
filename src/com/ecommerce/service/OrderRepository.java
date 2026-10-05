package com.ecommerce.service;

import com.ecommerce.model.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderRepository {

    private final Map<Integer, Order> orders;

    public OrderRepository() {
        orders = new HashMap<>();
    }

    public boolean save(Order order) {

        if (order == null) {
            return false;
        }

        orders.put(order.getOrderId(), order);

        return true;
    }

    public Order findById(int orderId) {

        return orders.get(orderId);
    }

    public boolean exists(int orderId) {

        return orders.containsKey(orderId);
    }

    public Order removeById(int orderId) {

        return orders.remove(orderId);
    }

    public List<Order> findAll() {

        return new ArrayList<>(orders.values());
    }

    public List<Order> findByCustomerId(int customerId) {

        List<Order> customerOrders = new ArrayList<>();

        for (Order order : orders.values()) {

            if (order.getCustomer().getUserId() == customerId) {

                customerOrders.add(order);
            }
        }

        return customerOrders;
    }

    public int count() {

        return orders.size();
    }

    public void clear() {

        orders.clear();
    }
}