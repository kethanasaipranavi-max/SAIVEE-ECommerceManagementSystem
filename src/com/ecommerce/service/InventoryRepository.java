package com.ecommerce.service;

import com.ecommerce.model.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryRepository {

    private final Map<Integer, Inventory> inventories;

    public InventoryRepository() {
        inventories = new HashMap<>();
    }

    public boolean save(Inventory inventory) {

        if (inventory == null
                || inventory.getProduct() == null) {

            return false;
        }

        int productId =
                inventory.getProduct().getProductId();

        inventories.put(productId, inventory);

        return true;
    }

    public Inventory findByProductId(int productId) {

        return inventories.get(productId);
    }

    public boolean exists(int productId) {

        return inventories.containsKey(productId);
    }

    public Inventory removeByProductId(int productId) {

        return inventories.remove(productId);
    }

    public List<Inventory> findAll() {

        return new ArrayList<>(inventories.values());
    }

    public int count() {

        return inventories.size();
    }

    public void clear() {

        inventories.clear();
    }
}