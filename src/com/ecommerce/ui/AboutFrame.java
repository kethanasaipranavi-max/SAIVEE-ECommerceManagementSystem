package com.ecommerce.ui;

import com.ecommerce.util.AppConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AboutFrame extends JFrame {

    private static final Color NAVY =
            new Color(13, 45, 82);

    private static final Color DARK_BLUE =
            new Color(25, 70, 125);

    private static final Color BLUE =
            new Color(45, 120, 210);

    private static final Color LIGHT_BLUE =
            new Color(220, 240, 255);

    private static final Color PALE_BLUE =
            new Color(245, 250, 255);

    public AboutFrame() {

        setTitle(
                "SAIVEE - About the System"
        );

        setSize(
                850,
                650
        );

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);

        setLayout(
                new BorderLayout()
        );

        createHeader();
        createContent();
        createFooter();
    }

    // ==================================================
    // HEADER
    // ==================================================

    private void createHeader() {

        JPanel headerPanel =
                new JPanel(
                        new BorderLayout()
                );

        headerPanel.setBackground(
                NAVY
        );

        headerPanel.setBorder(
                new EmptyBorder(
                        25,
                        35,
                        25,
                        35
                )
        );

        JLabel logoLabel =
                new JLabel("SAIVEE");

        logoLabel.setForeground(
                Color.WHITE
        );

        logoLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        32
                )
        );

        JLabel subtitleLabel =
                new JLabel(
                        "E-Commerce Order & Inventory Management System"
                );

        subtitleLabel.setForeground(
                LIGHT_BLUE
        );

        subtitleLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        JPanel titlePanel =
                new JPanel(
                        new GridLayout(
                                2,
                                1
                        )
                );

        titlePanel.setOpaque(false);

        titlePanel.add(
                logoLabel
        );

        titlePanel.add(
                subtitleLabel
        );

        JLabel versionLabel =
                new JLabel(
                        "Version "
                                + AppConstants.APPLICATION_VERSION,
                        SwingConstants.RIGHT
                );

        versionLabel.setForeground(
                Color.WHITE
        );

        versionLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        headerPanel.add(
                titlePanel,
                BorderLayout.WEST
        );

        headerPanel.add(
                versionLabel,
                BorderLayout.EAST
        );

        add(
                headerPanel,
                BorderLayout.NORTH
        );
    }

    // ==================================================
    // CONTENT
    // ==================================================

    private void createContent() {

        JPanel contentPanel =
                new JPanel(
                        new BorderLayout(
                                20,
                                20
                        )
                );

        contentPanel.setBackground(
                PALE_BLUE
        );

        contentPanel.setBorder(
                new EmptyBorder(
                        25,
                        30,
                        20,
                        30
                )
        );

        JTextArea aboutArea =
                new JTextArea();

        aboutArea.setEditable(false);

        aboutArea.setLineWrap(true);

        aboutArea.setWrapStyleWord(true);

        aboutArea.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        aboutArea.setForeground(
                NAVY
        );

        aboutArea.setBackground(
                Color.WHITE
        );

        aboutArea.setBorder(
                new EmptyBorder(
                        20,
                        25,
                        20,
                        25
                )
        );

        aboutArea.setText(
                "ABOUT SAIVEE\n"
                        + "==============================\n\n"

                        + "SAIVEE is a Java-based E-Commerce "
                        + "Order & Inventory Management System "
                        + "designed to simulate the core workflow "
                        + "of a real-world online commerce platform.\n\n"

                        + "The application provides separate "
                        + "customer and administrator workflows "
                        + "for managing products, inventory, "
                        + "shopping carts, orders, payments, "
                        + "deliveries, notifications and reports.\n\n"

                        + "CORE BUSINESS WORKFLOW\n"
                        + "------------------------------\n"
                        + "Customer Registration → Login → "
                        + "Product Search → Cart → Stock Reservation "
                        + "→ Payment → Order Creation → "
                        + "Order Processing → Delivery → "
                        + "Order Completion\n\n"

                        + "ORDER LIFECYCLE\n"
                        + "------------------------------\n"
                        + "PLACED → CONFIRMED → PROCESSING → "
                        + "SHIPPED → OUT_FOR_DELIVERY → DELIVERED\n\n"

                        + "The system also handles cancelled "
                        + "and failed orders while maintaining "
                        + "inventory consistency.\n\n"

                        + "MAJOR FUNCTIONAL MODULES\n"
                        + "------------------------------\n"
                        + "• Customer Management\n"
                        + "• Authentication & Session Management\n"
                        + "• Product Catalogue\n"
                        + "• Product Search & Filtering\n"
                        + "• Shopping Cart Management\n"
                        + "• Inventory & Stock Reservation\n"
                        + "• Order Processing\n"
                        + "• Payment Processing\n"
                        + "• Delivery Management\n"
                        + "• Notification Management\n"
                        + "• Wishlist Management\n"
                        + "• Reporting\n"
                        + "• Administrator Management\n\n"

                        + "TECHNICAL CONCEPTS DEMONSTRATED\n"
                        + "------------------------------\n"
                        + "• Object-Oriented Programming\n"
                        + "• Encapsulation\n"
                        + "• Inheritance\n"
                        + "• Polymorphism\n"
                        + "• Abstraction\n"
                        + "• Interfaces\n"
                        + "• Abstract Classes\n"
                        + "• Method Overriding\n"
                        + "• Collections Framework\n"
                        + "• Exception Handling\n"
                        + "• Multithreading & Synchronization\n"
                        + "• Java I/O & NIO File Handling\n"
                        + "• Packages & Modular Design\n"
                        + "• Java Swing GUI Development\n"
                        + "• Service-Layer Architecture\n"
                        + "• Input Validation\n"
                        + "• Business Rule Enforcement\n\n"

                        + "RELIABILITY & BUSINESS RULES\n"
                        + "------------------------------\n"
                        + "• Prevents orders beyond available stock\n"
                        + "• Supports atomic stock reservation and release\n"
                        + "• Restores stock when an order is cancelled\n"
                        + "• Prevents invalid order-status transitions\n"
                        + "• Prevents duplicate payment IDs\n"
                        + "• Validates customer and product data\n"
                        + "• Restricts administrative operations\n"
                        + "• Maintains customer-specific order visibility\n"
                        + "• Handles concurrent inventory operations safely\n\n"

                        + "TECHNOLOGY STACK\n"
                        + "------------------------------\n"
                        + "Language: Java\n"
                        + "GUI: Java Swing\n"
                        + "Persistence: Java File I/O / NIO\n"
                        + "Data Structures: Java Collections Framework\n"
                        + "Concurrency: Threads / Synchronization\n"
                        + "Architecture: Model + Service + UI layers\n\n"

                        + "PROJECT OBJECTIVE\n"
                        + "------------------------------\n"
                        + "The objective of SAIVEE is to demonstrate "
                        + "how core Java concepts can be combined "
                        + "to build a structured, maintainable and "
                        + "realistic business application rather "
                        + "than a simple academic console program.\n\n"

                        + "SAIVEE demonstrates the complete journey "
                        + "of an e-commerce transaction from product "
                        + "selection to successful delivery."
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        aboutArea
                );

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        LIGHT_BLUE,
                        2
                )
        );

        contentPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        JPanel highlightPanel =
                createHighlightPanel();

        contentPanel.add(
                highlightPanel,
                BorderLayout.SOUTH
        );

        add(
                contentPanel,
                BorderLayout.CENTER
        );
    }

    // ==================================================
    // HIGHLIGHT PANEL
    // ==================================================

    private JPanel createHighlightPanel() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                10,
                                10
                        )
                );

        panel.setOpaque(false);

        panel.add(
                createHighlight(
                        "CUSTOMER",
                        "Shopping & Orders"
                )
        );

        panel.add(
                createHighlight(
                        "INVENTORY",
                        "Stock Control"
                )
        );

        panel.add(
                createHighlight(
                        "PAYMENT",
                        "Transaction Flow"
                )
        );

        panel.add(
                createHighlight(
                        "ADMIN",
                        "System Control"
                )
        );

        return panel;
    }

    private JPanel createHighlight(
            String title,
            String description) {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                2,
                                1
                        )
                );

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BLUE,
                                1
                        ),
                        new EmptyBorder(
                                8,
                                8,
                                8,
                                8
                        )
                )
        );

        JLabel titleLabel =
                new JLabel(
                        title,
                        SwingConstants.CENTER
                );

        titleLabel.setForeground(
                DARK_BLUE
        );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        JLabel descriptionLabel =
                new JLabel(
                        description,
                        SwingConstants.CENTER
                );

        descriptionLabel.setForeground(
                NAVY
        );

        descriptionLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        11
                )
        );

        panel.add(
                titleLabel
        );

        panel.add(
                descriptionLabel
        );

        return panel;
    }

    // ==================================================
    // FOOTER
    // ==================================================

    private void createFooter() {

        JPanel footerPanel =
                new JPanel(
                        new BorderLayout()
                );

        footerPanel.setBackground(
                NAVY
        );

        footerPanel.setBorder(
                new EmptyBorder(
                        12,
                        20,
                        12,
                        20
                )
        );

        JLabel footerLabel =
                new JLabel(
                        "SAIVEE • Built with Core Java • "
                                + "Designed as a real-world business application",
                        SwingConstants.CENTER
                );

        footerLabel.setForeground(
                LIGHT_BLUE
        );

        footerLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        footerPanel.add(
                footerLabel,
                BorderLayout.CENTER
        );

        JButton closeButton =
                new JButton("Close");

        closeButton.setFocusPainted(false);

        closeButton.addActionListener(
                e -> dispose()
        );

        footerPanel.add(
                closeButton,
                BorderLayout.EAST
        );

        add(
                footerPanel,
                BorderLayout.SOUTH
        );
    }
}