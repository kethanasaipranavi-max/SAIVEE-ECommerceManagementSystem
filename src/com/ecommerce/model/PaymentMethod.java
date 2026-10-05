package com.ecommerce.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class PaymentMethod {

    private String paymentId;
    protected double amount;
    private boolean successful;
    private PaymentStatus status;
    private LocalDateTime paymentDateTime;

    public PaymentMethod() {
        successful = false;
        status = PaymentStatus.PENDING;
        paymentDateTime = null;
    }

    public PaymentMethod(
            String paymentId,
            double amount) {

        this.paymentId = paymentId;
        this.amount = amount;
        this.successful = false;
        this.status = PaymentStatus.PENDING;
        this.paymentDateTime = null;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public PaymentStatus getStatus() { return status; }

    public void markFailed() { status = PaymentStatus.FAILED; successful = false; }

    public void markRefunded() {
        status = PaymentStatus.REFUNDED;
        successful = false;
    }

    public LocalDateTime getPaymentDateTime() {
        return paymentDateTime;
    }

    protected void setSuccessful(
            boolean successful) {

        this.successful = successful;
        this.status = successful ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;

        if (successful) {
            paymentDateTime = LocalDateTime.now();
        }
    }

    public abstract boolean processPayment();

    // =========================================================
    // PAYMENT RECEIPT
    // =========================================================

    public String generateReceipt() {

        String dateTime =
                paymentDateTime == null
                        ? "Not available"
                        : paymentDateTime.format(
                                DateTimeFormatter.ofPattern(
                                        "dd-MM-yyyy HH:mm:ss"
                                )
                        );

        return
                "========================================\n"
                + "              SAIVEE RECEIPT             \n"
                + "========================================\n"
                + "Payment ID      : " + paymentId + "\n"
                + "Amount          : ₹"
                + String.format("%.2f", amount)
                + "\n"
                + "Payment Status  : " + status
                + "\n"
                + "Payment Date    : " + dateTime + "\n"
                + "Payment Method  : "
                + getPaymentMethodName()
                + "\n"
                + "========================================\n"
                + "        Thank you for shopping with      \n"
                + "                  SAIVEE                 \n"
                + "========================================";
    }

    protected String getPaymentMethodName() {
        return getClass().getSimpleName();
    }

    public void displayReceipt() {
        System.out.println(generateReceipt());
    }

    // =========================================================
    // PAYMENT INFORMATION
    // =========================================================

    public void displayPaymentInfo() {

        System.out.println(
                "Payment ID: " + paymentId
        );

        System.out.println(
                "Amount: ₹"
                        + String.format(
                                "%.2f",
                                amount
                        )
        );

        System.out.println(
                "Payment Status: " + status
        );

        if (paymentDateTime != null) {

            System.out.println(
                    "Payment Date: "
                            + paymentDateTime.format(
                                    DateTimeFormatter.ofPattern(
                                            "dd-MM-yyyy HH:mm:ss"
                                    )
                            )
            );
        }
    }

    @Override
    public String toString() {

        return "Payment ID: "
                + paymentId
                + "\nAmount: ₹"
                + String.format(
                        "%.2f",
                        amount
                )
                + "\nStatus: " + status;
    }
}