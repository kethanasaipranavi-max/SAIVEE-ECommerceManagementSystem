package com.ecommerce.service;

import com.ecommerce.model.Admin;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderStatus;
import com.ecommerce.model.Product;

public class AdminService {

    private final UserService userService;
    private final ProductService productService;
    private final InventoryService inventoryService;
    private final OrderService orderService;

    public AdminService(
            UserService userService,
            ProductService productService,
            InventoryService inventoryService,
            OrderService orderService) {

        this.userService = userService;
        this.productService = productService;
        this.inventoryService = inventoryService;
        this.orderService = orderService;
    }

    // =========================================================
    // ADMIN AUTHENTICATION
    // =========================================================

    public boolean isAdmin(int adminId) {

        if (userService == null) {
            return false;
        }

        Admin admin =
                userService.findAdminById(adminId);

        return admin != null;
    }

    // =========================================================
    // ADD PRODUCT
    // =========================================================

    public boolean addProduct(
            int adminId,
            Product product) {

        if (!isAdmin(adminId)) {

            System.out.println(
                    "Access denied. Only admins can add products."
            );

            return false;
        }

        if (productService == null) {

            System.out.println(
                    "Product service not available."
            );

            return false;
        }

        if (product == null) {

            System.out.println(
                    "Product cannot be null."
            );

            return false;
        }

        return productService.addProduct(product);
    }

    // =========================================================
    // REMOVE PRODUCT
    // =========================================================

    public boolean removeProduct(
            int adminId,
            int productId) {

        if (!isAdmin(adminId)) {

            System.out.println(
                    "Access denied. Only admins can remove products."
            );

            return false;
        }

        if (productService == null) {

            System.out.println(
                    "Product service not available."
            );

            return false;
        }

        return productService.removeProduct(
                productId
        );
    }

    // =========================================================
    // UPDATE PRODUCT
    // =========================================================

    public boolean updateProduct(
            int adminId,
            Product product) {

        if (!isAdmin(adminId)) {

            System.out.println(
                    "Access denied. Only admins can update products."
            );

            return false;
        }

        if (productService == null) {

            System.out.println(
                    "Product service not available."
            );

            return false;
        }

        if (product == null) {

            System.out.println(
                    "Product cannot be null."
            );

            return false;
        }

        return productService.updateProduct(
                product
        );
    }

    // =========================================================
    // ADD INVENTORY
    // =========================================================

    public boolean addInventory(
            int adminId,
            Product product,
            int stock) {

        if (!isAdmin(adminId)) {

            System.out.println(
                    "Access denied. Only admins can manage inventory."
            );

            return false;
        }

        if (inventoryService == null) {

            System.out.println(
                    "Inventory service not available."
            );

            return false;
        }

        if (product == null
                || stock < 0) {

            System.out.println(
                    "Invalid inventory details."
            );

            return false;
        }

        inventoryService.addInventory(
                product,
                stock
        );

        return inventoryService.findInventoryByProductId(
                product.getProductId()
        ) != null;
    }

    // =========================================================
    // UPDATE STOCK
    // =========================================================

    public boolean updateStock(
            int adminId,
            int productId,
            int quantity) {

        if (!isAdmin(adminId)) {

            System.out.println(
                    "Access denied. Only admins can update stock."
            );

            return false;
        }

        if (inventoryService == null) {

            System.out.println(
                    "Inventory service not available."
            );

            return false;
        }

        if (quantity < 0) {

            System.out.println(
                    "Stock quantity cannot be negative."
            );

            return false;
        }

        return inventoryService.updateStock(
                productId,
                quantity
        );
    }

    // =========================================================
    // REMOVE STOCK
    // =========================================================

    public boolean removeStock(
            int adminId,
            int productId,
            int quantity) {

        if (!isAdmin(adminId)) {

            System.out.println(
                    "Access denied. Only admins can manage inventory."
            );

            return false;
        }

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
    // DISPLAY PRODUCTS
    // =========================================================

    public void displayAdminProducts(
            int adminId) {

        if (!isAdmin(adminId)) {

            System.out.println(
                    "Access denied."
            );

            return;
        }

        if (productService == null) {

            System.out.println(
                    "Product service not available."
            );

            return;
        }

        productService.displayAllProducts();
    }

    // =========================================================
    // DISPLAY INVENTORY
    // =========================================================

    public void displayInventory(
            int adminId) {

        if (!isAdmin(adminId)) {

            System.out.println(
                    "Access denied."
            );

            return;
        }

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

    public void displayLowStockProducts(
            int adminId) {

        if (!isAdmin(adminId)) {

            System.out.println(
                    "Access denied."
            );

            return;
        }

        if (inventoryService == null) {

            System.out.println(
                    "Inventory service not available."
            );

            return;
        }

        inventoryService.displayLowStockProducts();
    }

    // =========================================================
    // VIEW ALL ORDERS
    // =========================================================

    public void displayAllOrders(
            int adminId) {

        if (!isAdmin(adminId)) {

            System.out.println(
                    "Access denied."
            );

            return;
        }

        if (orderService == null) {

            System.out.println(
                    "Order service not available."
            );

            return;
        }

        orderService.displayAllOrders();
    }

    // =========================================================
    // FIND ORDER
    // =========================================================

    public Order findOrder(
            int adminId,
            int orderId) {

        if (!isAdmin(adminId)) {

            System.out.println(
                    "Access denied."
            );

            return null;
        }

        if (orderService == null) {

            System.out.println(
                    "Order service not available."
            );

            return null;
        }

        return orderService.findOrderById(
                orderId
        );
    }

    // =========================================================
    // UPDATE ORDER STATUS
    // =========================================================

    public boolean updateOrderStatus(
            int adminId,
            int orderId,
            OrderStatus status) {

        if (!isAdmin(adminId)) {

            System.out.println(
                    "Access denied."
            );

            return false;
        }

        if (orderService == null) {

            System.out.println(
                    "Order service not available."
            );

            return false;
        }

        if (status == null) {

            System.out.println(
                    "Order status cannot be null."
            );

            return false;
        }

        return orderService.updateOrderStatus(
                orderId,
                status
        );
    }

    // =========================================================
    // GET PRODUCT COUNT
    // =========================================================

    public int getProductCount(
            int adminId) {

        if (!isAdmin(adminId)
                || productService == null) {

            return 0;
        }

        return productService.getProductCount();
    }

    // =========================================================
    // GET ORDER COUNT
    // =========================================================

    public int getOrderCount(
            int adminId) {

        if (!isAdmin(adminId)
                || orderService == null) {

            return 0;
        }

        return orderService.getOrderCount();
    }

    // =========================================================
    // GET INVENTORY COUNT
    // =========================================================

    public int getInventoryCount(
            int adminId) {

        if (!isAdmin(adminId)
                || inventoryService == null) {

            return 0;
        }

        return inventoryService.getInventoryCount();
    }
}