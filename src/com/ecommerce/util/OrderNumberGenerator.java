package com.ecommerce.util;

public final class OrderNumberGenerator {

    private static int nextOrderId = 1000;

    private OrderNumberGenerator() {
        // Prevent object creation.
    }

    public static synchronized int generateOrderId() {

        return nextOrderId++;
    }

    public static synchronized int getNextOrderId() {

        return nextOrderId;
    }
}