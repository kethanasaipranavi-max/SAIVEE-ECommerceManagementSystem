package com.ecommerce.service;

import com.ecommerce.exception.InsufficientStockException;
import com.ecommerce.model.Order;

public class OrderProcessingThread extends Thread {

    private final OrderProcessingService orderProcessingService;
    private final int orderId;
    private final int cartId;

    public OrderProcessingThread(
            OrderProcessingService orderProcessingService,
            int orderId,
            int cartId) {

        this.orderProcessingService = orderProcessingService;
        this.orderId = orderId;
        this.cartId = cartId;
    }

    @Override
    public void run() {

        System.out.println(
                "Processing order in thread: "
                + Thread.currentThread().getName()
        );

        try {

            Order order =
                    orderProcessingService.placeOrder(
                            orderId,
                            cartId
                    );

            if (order != null) {

                System.out.println(
                        "Order " + order.getOrderId()
                        + " processed successfully."
                );
            }

        } catch (InsufficientStockException e) {

            System.out.println(
                    "Order processing failed: "
                    + e.getMessage()
            );
        }
    }
}