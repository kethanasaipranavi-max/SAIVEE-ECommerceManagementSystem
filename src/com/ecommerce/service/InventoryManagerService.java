package com.ecommerce.service;

import com.ecommerce.exception.InsufficientStockException;

public class InventoryManagerService {

    private final InventoryService inventoryService;

    public InventoryManagerService(
            InventoryService inventoryService) {

        this.inventoryService = inventoryService;
    }

    // =========================================================
    // ADD STOCK
    // =========================================================

    public boolean addStock(
            int productId,
            int quantity) {

        if (inventoryService == null) {

            System.out.println(
                    "Inventory service not available."
            );

            return false;
        }

        if (quantity <= 0) {

            System.out.println(
                    "Stock quantity must be greater than zero."
            );

            return false;
        }

        return inventoryService.addStock(
                productId,
                quantity
        );
    }

    // =========================================================
    // REMOVE STOCK
    // =========================================================

    public boolean removeStock(
            int productId,
            int quantity) {

        if (inventoryService == null) {

            System.out.println(
                    "Inventory service not available."
            );

            return false;
        }

        if (quantity <= 0) {

            System.out.println(
                    "Stock quantity must be greater than zero."
            );

            return false;
        }

        return inventoryService.removeStock(
                productId,
                quantity
        );
    }

    // =========================================================
    // RESERVE STOCK
    // =========================================================

    public boolean reserveStock(
            int productId,
            int quantity) {

        if (inventoryService == null) {

            System.out.println(
                    "Inventory service not available."
            );

            return false;
        }

        if (quantity <= 0) {

            System.out.println(
                    "Stock quantity must be greater than zero."
            );

            return false;
        }

        try {

            return inventoryService.reserveStock(
                    productId,
                    quantity
            );

        } catch (InsufficientStockException e) {

            System.out.println(
                    "Stock reservation failed: "
                            + e.getMessage()
            );

            return false;
        }
    }

    // =========================================================
    // RELEASE RESERVED STOCK
    // =========================================================

    public boolean releaseReservedStock(
            int productId,
            int quantity) {

        if (inventoryService == null) {

            System.out.println(
                    "Inventory service not available."
            );

            return false;
        }

        if (quantity <= 0) {

            System.out.println(
                    "Stock quantity must be greater than zero."
            );

            return false;
        }

        return inventoryService.releaseReservedStock(
                productId,
                quantity
        );
    }

    // =========================================================
    // RESTORE STOCK
    // =========================================================

    public boolean restoreStock(
            int productId,
            int quantity) {

        if (inventoryService == null) {

            System.out.println(
                    "Inventory service not available."
            );

            return false;
        }

        if (quantity <= 0) {

            System.out.println(
                    "Stock quantity must be greater than zero."
            );

            return false;
        }

        return inventoryService.restoreStock(
                inventoryService.findProductById(productId),
                quantity
        );
    }

    // =========================================================
    // CHECK STOCK
    // =========================================================

    public boolean hasEnoughStock(
            int productId,
            int quantity) {

        if (inventoryService == null
                || quantity <= 0) {

            return false;
        }

        return inventoryService.hasEnoughStock(
                productId,
                quantity
        );
    }

    // =========================================================
    // DISPLAY INVENTORY
    // =========================================================

    public void displayInventory() {

        if (inventoryService == null) {

            System.out.println(
                    "Inventory service not available."
            );

            return;
        }

        inventoryService.displayAllInventory();
    }

    // =========================================================
    // DISPLAY LOW STOCK
    // =========================================================

    public void displayLowStock(
            int threshold) {

        if (inventoryService == null) {

            System.out.println(
                    "Inventory service not available."
            );

            return;
        }

        if (threshold < 0) {

            System.out.println(
                    "Stock threshold cannot be negative."
            );

            return;
        }

        inventoryService.displayLowStock(
                threshold
        );
    }
}