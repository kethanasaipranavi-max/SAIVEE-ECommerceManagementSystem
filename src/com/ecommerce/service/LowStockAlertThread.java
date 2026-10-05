package com.ecommerce.service;

import com.ecommerce.model.Product;
import java.util.HashSet;
import java.util.Set;

public class LowStockAlertThread extends Thread {
    private final InventoryService inventoryService;
    private final int threshold;
    private final long intervalMillis;
    private final Set<Integer> alerted = new HashSet<>();
    private volatile boolean running = true;

    public LowStockAlertThread(InventoryService inventoryService, int threshold) {
        this(inventoryService, threshold, 15000L);
    }

    public LowStockAlertThread(InventoryService inventoryService, int threshold, long intervalMillis) {
        this.inventoryService = inventoryService;
        this.threshold = Math.max(0, threshold);
        this.intervalMillis = Math.max(3000L, intervalMillis);
        setName("SAIVEE-LowStockMonitor");
        setDaemon(true);
    }

    @Override public void run() {
        while (running) {
            try {
                if (inventoryService != null) {
                    for (Product p : inventoryService.getLowStockProducts()) {
                        if (p != null && p.getQuantity() <= threshold && alerted.add(p.getProductId())) {
                            String message = "LOW STOCK ALERT: " + p.getProductName()
                                    + " | Stock: " + p.getQuantity();
                            System.out.println(message);
                            AuditLogService.getInstance().log("SYSTEM", message);
                        }
                    }
                    alerted.removeIf(id -> {
                        Product p = inventoryService.findProductById(id);
                        return p == null || p.getQuantity() > threshold;
                    });
                }
                Thread.sleep(intervalMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void shutdown() { running = false; interrupt(); }
}
