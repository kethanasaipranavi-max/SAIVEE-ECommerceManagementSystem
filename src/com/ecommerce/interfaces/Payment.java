package com.ecommerce.interfaces;

public interface Payment {

    boolean processPayment(double amount);

    void generateReceipt();
}