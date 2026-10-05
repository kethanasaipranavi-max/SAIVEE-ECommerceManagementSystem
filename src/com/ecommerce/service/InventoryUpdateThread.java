package com.ecommerce.service;

public class InventoryUpdateThread extends Thread {

    private final InventoryManagerService inventoryManagerService;
    private final int productId;
    private final int quantity;
    private final boolean addStock;

    public InventoryUpdateThread(
            InventoryManagerService inventoryManagerService,
            int productId,
            int quantity,
            boolean addStock) {

        this.inventoryManagerService = inventoryManagerService;
        this.productId = productId;
        this.quantity = quantity;
        this.addStock = addStock;
    }

    @Override
    public void run() {

        System.out.println(
                "Updating inventory in thread: "
                + Thread.currentThread().getName()
        );

        if (addStock) {

            inventoryManagerService.addStock(
                    productId,
                    quantity
            );

        } else {

            inventoryManagerService.removeStock(
                    productId,
                    quantity
            );
        }

        System.out.println(
                "Inventory update completed."
        );
    }
}