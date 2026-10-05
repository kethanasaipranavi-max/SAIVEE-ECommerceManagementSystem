package com.ecommerce.util;

import java.time.LocalDateTime;

public final class ApplicationLogger {

    private ApplicationLogger() {
        // Prevent object creation.
    }

    public static void info(String message) {

        System.out.println(
                "[" + LocalDateTime.now() + "] INFO: "
                + message
        );
    }

    public static void warning(String message) {

        System.out.println(
                "[" + LocalDateTime.now() + "] WARNING: "
                + message
        );
    }

    public static void error(String message) {

        System.out.println(
                "[" + LocalDateTime.now() + "] ERROR: "
                + message
        );
    }
}