package com.ecommerce.util;

public final class AppConstants {

    public static final String APPLICATION_NAME =
            "E-Commerce Order & Inventory Management System";

    public static final String APPLICATION_VERSION =
            "1.0";

    public static final int LOW_STOCK_THRESHOLD = 5;

    public static final int MIN_PASSWORD_LENGTH = 6;

    public static final double MAX_DISCOUNT_PERCENTAGE = 100.0;

    public static final String CURRENCY_SYMBOL = "₹";

    private AppConstants() {
        // Prevent object creation.
    }
}