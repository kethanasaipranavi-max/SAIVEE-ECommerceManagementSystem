package com.ecommerce.service;

public class OrderCancellationThread extends Thread {

    private final OrderCancellationService cancellationService;
    private final int orderId;

    public OrderCancellationThread(
            OrderCancellationService cancellationService,
            int orderId) {

        this.cancellationService = cancellationService;
        this.orderId = orderId;
    }

    @Override
    public void run() {

        System.out.println(
                "Cancelling order in thread: "
                + Thread.currentThread().getName()
        );

        cancellationService.cancelOrder(orderId);

        System.out.println(
                "Order cancellation completed."
        );
    }
}