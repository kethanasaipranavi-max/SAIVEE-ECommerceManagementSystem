package com.ecommerce.service;

import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.model.OrderStatus;

public class OrderCancellationService {

    private final OrderService orderService;
    private final InventoryService inventoryService;

    public OrderCancellationService(
            OrderService orderService,
            InventoryService inventoryService) {

        this.orderService = orderService;
        this.inventoryService = inventoryService;
    }

    public synchronized void cancelOrder(int orderId) {

        Order order =
                orderService.findOrderById(orderId);

        if (order == null) {

            System.out.println(
                    "Order not found."
            );

            return;
        }

        /*
         * Prevent duplicate cancellation.
         * This also prevents restoring stock twice.
         */
        if (order.getStatus()
                == OrderStatus.CANCELLED) {

            System.out.println(
                    "Order is already cancelled."
            );

            return;
        }

        /*
         * Delivered orders cannot be cancelled.
         */
        if (order.getStatus()
                == OrderStatus.DELIVERED) {

            System.out.println(
                    "Delivered order cannot be cancelled."
            );

            return;
        }

        /*
         * Release reserved stock for every
         * item in the order.
         */
        for (OrderItem item :
                order.getOrderItems()) {

            int productId =
                    item.getProduct()
                            .getProductId();

            int quantity =
                    item.getQuantity();

            inventoryService
                    .releaseReservedStock(
                            productId,
                            quantity
                    );
        }

        /*
         * Change the order status only after
         * reserved stock has been released.
         */
        order.updateStatus(
                OrderStatus.CANCELLED
        );

        System.out.println(
                "Order cancelled successfully."
        );

        System.out.println(
                "Reserved stock has been restored."
        );
    }
}