package com.ecommerce.model;

public class CardPayment extends PaymentMethod {

    private String cardNumber;
    private String cardHolderName;

    public CardPayment() {
        super();
    }

    public CardPayment(
            String paymentId,
            double amount,
            String cardNumber,
            String cardHolderName) {

        super(paymentId, amount);

        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    @Override
    public boolean processPayment() {

        System.out.println(
                "Processing card payment..."
        );

        System.out.println(
                "Card Holder: "
                        + cardHolderName
        );

        System.out.println(
                "Amount: ₹"
                        + String.format(
                                "%.2f",
                                amount
                        )
        );

        /*
         * Simulated payment processing.
         * In a real payment gateway, this would
         * communicate with the payment provider.
         */
        setSuccessful(true);

        return true;
    }

    @Override
    protected String getPaymentMethodName() {
        return "Card";
    }

    @Override
    public void displayPaymentInfo() {

        super.displayPaymentInfo();

        System.out.println(
                "Payment Type: Card"
        );

        System.out.println(
                "Card Holder: "
                        + cardHolderName
        );

        System.out.println(
                "Card Number: "
                        + maskCardNumber()
        );
    }

    private String maskCardNumber() {

        if (cardNumber == null
                || cardNumber.length() < 4) {

            return "****";
        }

        return "************"
                + cardNumber.substring(
                        cardNumber.length() - 4
                );
    }
}