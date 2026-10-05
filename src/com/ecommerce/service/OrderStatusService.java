package com.ecommerce.service;

import com.ecommerce.model.Order;
import com.ecommerce.model.OrderStatus;

public class OrderStatusService {

    private final OrderService orderService;

    public OrderStatusService(OrderService orderService) {
        this.orderService = orderService;
    }

    public void updateStatus(int orderId, OrderStatus newStatus) {

        Order order = orderService.findOrderById(orderId);

        if (order == null) {

            System.out.println("Order not found.");
            return;
        }

        if (newStatus == null) {

            System.out.println("Invalid order status.");
            return;
        }

        OrderStatus currentStatus = order.getStatus();

        if (!isValidTransition(currentStatus, newStatus)) {

            System.out.println(
                    "Invalid status transition: "
                    + currentStatus + " -> " + newStatus
            );

            return;
        }

        order.updateStatus(newStatus);

        System.out.println(
                "Order status changed from "
                + currentStatus
                + " to "
                + newStatus
        );
    }

    private boolean isValidTransition(OrderStatus currentStatus,
                                      OrderStatus newStatus) {

        if (currentStatus == OrderStatus.CANCELLED
                || currentStatus == OrderStatus.FAILED
                || currentStatus == OrderStatus.DELIVERED) {

            return false;
        }

        switch (currentStatus) {

            case PLACED:
                return newStatus == OrderStatus.CONFIRMED
                        || newStatus == OrderStatus.CANCELLED;

            case CONFIRMED:
                return newStatus == OrderStatus.PROCESSING
                        || newStatus == OrderStatus.CANCELLED;

            case PROCESSING:
                return newStatus == OrderStatus.SHIPPED;

            case SHIPPED:
                return newStatus == OrderStatus.OUT_FOR_DELIVERY;

            case OUT_FOR_DELIVERY:
                return newStatus == OrderStatus.DELIVERED;

            default:
                return false;
        }
    }
}