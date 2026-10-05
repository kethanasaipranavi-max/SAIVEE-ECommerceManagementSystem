package com.ecommerce.model;

public class UpiPayment extends PaymentMethod {

    private String upiId;

    public UpiPayment() {
        super();
    }

    public UpiPayment(
            String paymentId,
            double amount,
            String upiId) {

        super(paymentId, amount);
        this.upiId = upiId;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    @Override
    public boolean processPayment() {

        System.out.println(
                "Processing UPI payment..."
        );

        System.out.println(
                "UPI ID: " + upiId
        );

        System.out.println(
                "Amount: ₹"
                        + String.format(
                                "%.2f",
                                amount
                        )
        );

        setSuccessful(true);

        return true;
    }

    @Override
    protected String getPaymentMethodName() {
        return "UPI";
    }

    @Override
    public void displayPaymentInfo() {

        super.displayPaymentInfo();

        System.out.println(
                "Payment Type: UPI"
        );

        System.out.println(
                "UPI ID: " + upiId
        );
    }
}