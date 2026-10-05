package com.ecommerce.ui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.GridLayout;

public class DashboardFrame extends JFrame {

    private final JLabel welcomeLabel;
    private final JButton productsButton;
    private final JButton cartButton;
    private final JButton ordersButton;
    private final JButton logoutButton;

    public DashboardFrame(String userName) {

        setTitle("E-Commerce Dashboard");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        welcomeLabel = new JLabel(
                "Welcome, " + userName + "!",
                JLabel.CENTER
        );

        productsButton = new JButton("Products");
        cartButton = new JButton("Cart");
        ordersButton = new JButton("Orders");
        logoutButton = new JButton("Logout");

        JPanel buttonPanel = new JPanel(
                new GridLayout(2, 2, 10, 10)
        );

        buttonPanel.add(productsButton);
        buttonPanel.add(cartButton);
        buttonPanel.add(ordersButton);
        buttonPanel.add(logoutButton);

        setLayout(new BorderLayout(10, 10));

        add(welcomeLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }

    public JButton getProductsButton() {
        return productsButton;
    }

    public JButton getCartButton() {
        return cartButton;
    }

    public JButton getOrdersButton() {
        return ordersButton;
    }

    public JButton getLogoutButton() {
        return logoutButton;
    }
}