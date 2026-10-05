package com.ecommerce.util;

public final class ECommerceValidator {

    private ECommerceValidator() {
        // Prevent object creation.
    }

    public static boolean isValidId(int id) {

        return id > 0;
    }

    public static boolean isValidQuantity(int quantity) {

        return quantity > 0;
    }

    public static boolean isValidPrice(double price) {

        return price >= 0;
    }

    public static boolean isNotEmpty(String value) {

        return value != null
                && !value.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {

        if (!isNotEmpty(email)) {
            return false;
        }

        return email.contains("@")
                && email.contains(".");
    }

    public static boolean isValidPercentage(
            double percentage) {

        return percentage >= 0
                && percentage <= 100;
    }
}