package com.ecommerce.util;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public final class GlobalExceptionHandler {
    private GlobalExceptionHandler() {}

    public static void install() {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            ApplicationLogger.error("Unhandled exception in " + thread.getName()
                    + ": " + throwable.getMessage());
            throwable.printStackTrace();
            Runnable show = () -> JOptionPane.showMessageDialog(null,
                    "SAIVEE encountered an unexpected error.\n\n"
                            + (throwable.getMessage() == null
                            ? "Please try the operation again."
                            : throwable.getMessage()),
                    "SAIVEE - Application Error",
                    JOptionPane.ERROR_MESSAGE);
            if (SwingUtilities.isEventDispatchThread()) show.run();
            else SwingUtilities.invokeLater(show);
        });
    }
}
