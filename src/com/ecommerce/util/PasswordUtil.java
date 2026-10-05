package com.ecommerce.util;

public final class PasswordUtil {

    private PasswordUtil() {
        // Prevent object creation.
    }

    public static boolean isValidPassword(String password) {

        if (password == null) {
            return false;
        }

        if (password.length()
                < AppConstants.MIN_PASSWORD_LENGTH) {

            return false;
        }

        boolean hasLetter = false;
        boolean hasDigit = false;

        for (int i = 0; i < password.length(); i++) {

            char character = password.charAt(i);

            if (Character.isLetter(character)) {
                hasLetter = true;
            }

            if (Character.isDigit(character)) {
                hasDigit = true;
            }

            if (hasLetter && hasDigit) {
                break;
            }
        }

        return hasLetter && hasDigit;
    }

    public static boolean passwordsMatch(String first,
                                         String second) {

        if (first == null || second == null) {
            return false;
        }

        return first.equals(second);
    }
}