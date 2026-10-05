package com.ecommerce.service;

public class InventoryMonitorThread extends Thread {

    private final InventoryManagerService inventoryManagerService;
    private final int threshold;

    public InventoryMonitorThread(
            InventoryManagerService inventoryManagerService,
            int threshold) {

        this.inventoryManagerService = inventoryManagerService;
        this.threshold = threshold;
    }

    @Override
    public void run() {

        System.out.println(
                "Checking inventory in thread: "
                + Thread.currentThread().getName()
        );

        inventoryManagerService.displayLowStock(threshold);

        System.out.println(
                "Inventory monitoring completed."
        );
    }
}