package com.ecommerce.service;

import com.ecommerce.model.Delivery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliveryRepository {

    private final Map<Integer, Delivery> deliveries;

    public DeliveryRepository() {
        deliveries = new HashMap<>();
    }

    public boolean save(Delivery delivery) {

        if (delivery == null) {
            return false;
        }

        deliveries.put(
                delivery.getDeliveryId(),
                delivery
        );

        return true;
    }

    public Delivery findById(int deliveryId) {

        return deliveries.get(deliveryId);
    }

    public boolean exists(int deliveryId) {

        return deliveries.containsKey(deliveryId);
    }

    public Delivery removeById(int deliveryId) {

        return deliveries.remove(deliveryId);
    }

    public List<Delivery> findAll() {

        return new ArrayList<>(deliveries.values());
    }

    public int count() {

        return deliveries.size();
    }

    public void clear() {

        deliveries.clear();
    }
}