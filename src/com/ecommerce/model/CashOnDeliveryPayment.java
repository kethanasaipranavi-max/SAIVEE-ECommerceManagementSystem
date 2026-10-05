package com.ecommerce.model;

public class CashOnDeliveryPayment extends PaymentMethod {

    private String deliveryAddress;

    public CashOnDeliveryPayment() {
        super();
    }

    public CashOnDeliveryPayment(
            String paymentId,
            double amount,
            String deliveryAddress) {

        super(paymentId, amount);
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(
            String deliveryAddress) {

        this.deliveryAddress = deliveryAddress;
    }

    @Override
    public boolean processPayment() {

        System.out.println(
                "Processing Cash on Delivery payment..."
        );

        System.out.println(
                "Amount to collect: ₹"
                        + String.format(
                                "%.2f",
                                amount
                        )
        );

        System.out.println(
                "Delivery Address: "
                        + deliveryAddress
        );

        /*
         * COD is considered successful at checkout.
         * Actual cash collection happens during delivery.
         */
        setSuccessful(true);

        return true;
    }

    @Override
    protected String getPaymentMethodName() {
        return "Cash on Delivery";
    }

    @Override
    public void displayPaymentInfo() {

        super.displayPaymentInfo();

        System.out.println(
                "Payment Type: Cash on Delivery"
        );

        System.out.println(
                "Delivery Address: "
                        + deliveryAddress
        );
    }
}