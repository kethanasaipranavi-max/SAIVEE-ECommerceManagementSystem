package com.ecommerce.ui;

import com.ecommerce.model.Order;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OrderFrame extends JFrame {

    private final JTextArea orderArea;
    private final JButton closeButton;

    public OrderFrame(List<Order> orders) {

        setTitle("Orders");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        orderArea = new JTextArea();
        orderArea.setEditable(false);

        closeButton = new JButton("Close");

        displayOrders(orders);

        setLayout(new BorderLayout());

        add(new JScrollPane(orderArea), BorderLayout.CENTER);
        add(closeButton, BorderLayout.SOUTH);

        closeButton.addActionListener(e -> dispose());
    }

    private void displayOrders(List<Order> orders) {

        if (orders == null || orders.isEmpty()) {

            orderArea.setText("No orders available.");
            return;
        }

        StringBuilder output = new StringBuilder();

        for (Order order : orders) {

            output.append("----------------------------\n");

            output.append("Order ID: ")
                    .append(order.getOrderId())
                    .append("\n");

            output.append("Customer: ")
                    .append(order.getCustomer().getName())
                    .append("\n");

            output.append("Status: ")
                    .append(order.getStatus())
                    .append("\n");

            output.append("Total Amount: ₹")
                    .append(order.getTotalAmount())
                    .append("\n");
        }

        orderArea.setText(output.toString());
    }
}