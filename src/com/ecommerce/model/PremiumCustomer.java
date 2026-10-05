package com.ecommerce.model;

public class PremiumCustomer extends Customer {

    private double discountPercentage;

    public PremiumCustomer() {
        super();
    }

    public PremiumCustomer(
            int customerId,
            String name,
            String email,
            String phone,
            String password,
            double discountPercentage) {

        super(
                customerId,
                name,
                email,
                phone,
                password
        );

        this.discountPercentage =
                discountPercentage;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(
            double discountPercentage) {

        this.discountPercentage =
                discountPercentage;
    }

    public double calculateDiscount(
            double amount) {

        return amount *
                discountPercentage / 100;
    }

    @Override
    public void displayUserInfo() {

        super.displayUserInfo();

        System.out.println(
                "Premium Discount: "
                        + discountPercentage
                        + "%"
        );
    }

    @Override
    public String toString() {

        return super.toString()
                + "\nPremium Discount: "
                + discountPercentage
                + "%";
    }
}