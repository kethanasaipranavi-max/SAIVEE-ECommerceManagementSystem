package com.ecommerce;

import javax.swing.SwingUtilities;

import com.ecommerce.model.Admin;
import com.ecommerce.model.Product;
import com.ecommerce.service.*;
import com.ecommerce.ui.MainFrame;
import com.ecommerce.util.AppConstants;
import com.ecommerce.util.GlobalExceptionHandler;

public class Main {

    public static void main(String[] args) {
        GlobalExceptionHandler.install();

        SwingUtilities.invokeLater(() -> {
            UserService userService = new UserService();
            ProductService productService = new ProductService();
            NotificationService notificationService = new NotificationService();
            InventoryService inventoryService = new InventoryService();
            OrderService orderService =
                    new OrderService(notificationService, inventoryService);
            CartService cartService = new CartService();
            ReportService reportService = new ReportService();

            Admin admin = new Admin(
                    1,
                    "SAIVEE Admin",
                    "admin@saivee.com",
                    "Administrator"
            );
            userService.addUser(admin);

            // 16 main categories -> 90+ subcategories -> 3+ brands -> 2 products/brand.
            CatalogService.seedCatalog(productService);
            ReviewService.getInstance().rebuildProductRatings(productService);

            for (Product product : productService.getProducts()) {
                inventoryService.addProduct(product);
            }

            // Restore customer order history after customers and catalogue are loaded.
            orderService.loadPersistedOrders(userService, productService);
            notificationService.loadPersistedNotifications(userService);

            // Automatic background low-stock monitoring.
            LowStockAlertThread lowStockMonitor =
                    new LowStockAlertThread(
                            inventoryService,
                            AppConstants.LOW_STOCK_THRESHOLD
                    );
            lowStockMonitor.start();

            OrderProcessingService orderProcessingService =
                    new OrderProcessingService(
                            cartService,
                            inventoryService,
                            orderService
                    );

            MainFrame mainFrame = new MainFrame(
                    userService,
                    productService,
                    orderService,
                    notificationService,
                    cartService,
                    inventoryService,
                    orderProcessingService,
                    reportService
            );

            mainFrame.setVisible(true);
        });
    }
}
