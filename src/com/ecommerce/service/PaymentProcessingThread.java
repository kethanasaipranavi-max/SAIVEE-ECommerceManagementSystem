package com.ecommerce.service;

import com.ecommerce.model.Order;
import com.ecommerce.model.PaymentMethod;

public class PaymentProcessingThread extends Thread {

    private final PaymentProcessingService paymentProcessingService;
    private final Order order;
    private final PaymentMethod paymentMethod;

    public PaymentProcessingThread(
            PaymentProcessingService paymentProcessingService,
            Order order,
            PaymentMethod paymentMethod) {

        this.paymentProcessingService = paymentProcessingService;
        this.order = order;
        this.paymentMethod = paymentMethod;
    }

    @Override
    public void run() {

        System.out.println(
                "Processing payment in thread: "
                + Thread.currentThread().getName()
        );

        boolean success =
                paymentProcessingService.processOrderPayment(
                        order,
                        paymentMethod
                );

        System.out.println(
                "Payment processing completed. Success: "
                + success
        );
    }
}