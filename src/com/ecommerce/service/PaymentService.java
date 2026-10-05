package com.ecommerce.service;

import com.ecommerce.model.PaymentMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PaymentService {

    private final List<PaymentMethod> payments;

    public PaymentService() {
        payments = new ArrayList<>();
    }

    // =========================================================
    // PROCESS PAYMENT
    // =========================================================

    public synchronized boolean processPayment(
            PaymentMethod paymentMethod) {

        if (paymentMethod == null) {

            System.out.println(
                    "Invalid payment method."
            );

            return false;
        }

        if (paymentMethod.getPaymentId() == null
                || paymentMethod.getPaymentId().isBlank()) {

            System.out.println(
                    "Payment ID cannot be empty."
            );

            return false;
        }

        if (paymentMethod.getAmount() <= 0) {

            System.out.println(
                    "Payment amount must be greater than zero."
            );

            return false;
        }

        if (paymentExists(
                paymentMethod.getPaymentId()
        )) {

            System.out.println(
                    "Payment ID already exists."
            );

            return false;
        }

        try {
            if (paymentMethod.getStatus() == com.ecommerce.model.PaymentStatus.FAILED) {
                return false;
            }
            boolean result =
                    paymentMethod.processPayment();

            payments.add(paymentMethod);
            if (result && paymentMethod.isSuccessful()) {
                System.out.println("Payment recorded successfully.");
                return true;
            }
            System.out.println("Payment failed and was recorded with FAILED status.");
            return false;

        } catch (RuntimeException e) {

            System.out.println(
                    "Payment processing error: "
                            + e.getMessage()
            );

            return false;
        }
    }

    // =========================================================
    // FIND PAYMENT BY ID
    // =========================================================

    public synchronized PaymentMethod findPaymentById(
            String paymentId) {

        if (paymentId == null
                || paymentId.isBlank()) {

            return null;
        }

        for (PaymentMethod payment :
                payments) {

            if (payment != null
                    && paymentId.equals(
                            payment.getPaymentId()
                    )) {

                return payment;
            }
        }

        return null;
    }

    // =========================================================
    // CHECK WHETHER PAYMENT EXISTS
    // =========================================================

    public synchronized boolean paymentExists(
            String paymentId) {

        return findPaymentById(
                paymentId
        ) != null;
    }

    public synchronized boolean refundPayment(String paymentId) {
        PaymentMethod payment = findPaymentById(paymentId);
        if (payment == null || payment.getStatus() != com.ecommerce.model.PaymentStatus.SUCCESS) return false;
        payment.markRefunded();
        return true;
    }

    // =========================================================
    // DISPLAY ALL PAYMENTS
    // =========================================================

    public synchronized void displayAllPayments() {

        if (payments.isEmpty()) {

            System.out.println(
                    "No payments available."
            );

            return;
        }

        System.out.println(
                "===== SAIVEE PAYMENTS ====="
        );

        for (PaymentMethod payment :
                payments) {

            if (payment == null) {
                continue;
            }

            System.out.println(
                    "----------------------------"
            );

            payment.displayPaymentInfo();
        }

        System.out.println(
                "----------------------------"
        );
    }

    // =========================================================
    // GET PAYMENT COUNT
    // =========================================================

    public synchronized int getPaymentCount() {

        return payments.size();
    }

    // =========================================================
    // GET TOTAL SUCCESSFUL PAYMENTS
    // =========================================================

    public synchronized double getTotalPaymentAmount() {

        double total = 0.0;

        for (PaymentMethod payment :
                payments) {

            if (payment != null
                    && payment.isSuccessful()) {

                total += payment.getAmount();
            }
        }

        return total;
    }

    // =========================================================
    // GET PAYMENT LIST
    // =========================================================

    public synchronized List<PaymentMethod>
    getPayments() {

        return Collections.unmodifiableList(
                new ArrayList<>(
                        payments
                )
        );
    }
}