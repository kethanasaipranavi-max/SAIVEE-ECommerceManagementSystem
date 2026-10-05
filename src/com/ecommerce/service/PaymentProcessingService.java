package com.ecommerce.service;

import com.ecommerce.model.Order;
import com.ecommerce.model.OrderStatus;
import com.ecommerce.model.PaymentMethod;

public class PaymentProcessingService {

    private final PaymentService paymentService;

    public PaymentProcessingService(
            PaymentService paymentService) {

        this.paymentService =
                paymentService != null
                        ? paymentService
                        : new PaymentService();
    }

    // =========================================================
    // PROCESS ORDER PAYMENT
    // =========================================================

    public boolean processOrderPayment(
            Order order,
            PaymentMethod paymentMethod) {

        if (order == null) {

            System.out.println(
                    "Order not found."
            );

            return false;
        }

        if (paymentMethod == null) {

            System.out.println(
                    "Payment method not found."
            );

            return false;
        }

        // -----------------------------------------------------
        // CANCELLED ORDER
        // -----------------------------------------------------

        if (order.getStatus()
                == OrderStatus.CANCELLED) {

            System.out.println(
                    "Cannot process payment for a cancelled order."
            );

            return false;
        }

        // -----------------------------------------------------
        // ALREADY DELIVERED ORDER
        // -----------------------------------------------------

        if (order.getStatus()
                == OrderStatus.DELIVERED) {

            System.out.println(
                    "Payment cannot be processed for a delivered order."
            );

            return false;
        }

        // -----------------------------------------------------
        // ALREADY FAILED ORDER
        //
        // Allows a caller to create a new payment attempt
        // only if they explicitly call this method again.
        // -----------------------------------------------------

        System.out.println(
                "Processing payment for Order ID: "
                        + order.getOrderId()
        );

        System.out.println(
                "Payment Amount: ₹"
                        + String.format(
                                "%.2f",
                                paymentMethod.getAmount()
                        )
        );

        // -----------------------------------------------------
        // PROCESS PAYMENT
        // -----------------------------------------------------

        boolean paymentSuccessful =
                paymentService.processPayment(
                        paymentMethod
                );

        // -----------------------------------------------------
        // PAYMENT SUCCESS
        // -----------------------------------------------------

        if (paymentSuccessful) {

            order.updateStatus(
                    OrderStatus.CONFIRMED
            );

            System.out.println(
                    "Order payment successful."
            );

            System.out.println(
                    "Order status updated to CONFIRMED."
            );

        } else {

            // -------------------------------------------------
            // PAYMENT FAILURE
            // -------------------------------------------------

            order.updateStatus(
                    OrderStatus.FAILED
            );

            System.out.println(
                    "Order payment failed."
            );

            System.out.println(
                    "Order status updated to FAILED."
            );
        }

        return paymentSuccessful;
    }

    // =========================================================
    // CHECK PAYMENT STATUS
    // =========================================================

    public boolean isPaymentSuccessful(
            PaymentMethod paymentMethod) {

        if (paymentMethod == null) {
            return false;
        }

        return paymentMethod.isSuccessful();
    }

    // =========================================================
    // GET PAYMENT AMOUNT
    // =========================================================

    public double getPaymentAmount(
            PaymentMethod paymentMethod) {

        if (paymentMethod == null) {
            return 0.0;
        }

        return paymentMethod.getAmount();
    }
}