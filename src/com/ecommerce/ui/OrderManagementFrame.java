package com.ecommerce.ui;

import com.ecommerce.model.Order;
import com.ecommerce.service.OrderService;

import javax.swing.*;
import java.awt.*;

public class OrderManagementFrame extends JFrame {

    private final OrderService orderService;

    private final JTextArea orderArea;
    private final JButton refreshButton;
    private final JButton closeButton;

    public OrderManagementFrame(OrderService orderService) {

        this.orderService = orderService;

        setTitle("Order Management");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        orderArea = new JTextArea();
        orderArea.setEditable(false);

        refreshButton = new JButton("Refresh");
        closeButton = new JButton("Close");

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);

        setLayout(new BorderLayout());

        add(new JScrollPane(orderArea), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(
                e -> refreshOrders()
        );

        closeButton.addActionListener(
                e -> dispose()
        );

        refreshOrders();
    }

    private void refreshOrders() {

        orderArea.setText("");

        if (orderService == null) {

            orderArea.setText(
                    "Order service is not available."
            );

            return;
        }

        for (Order order : orderService.getOrders()) {

            orderArea.append(
                    "----------------------------\n"
                    + "Order ID: "
                    + order.getOrderId()
                    + "\n"
                    + "Customer: "
                    + order.getCustomer().getName()
                    + "\n"
                    + "Status: "
                    + order.getStatus()
                    + "\n"
                    + "Total: ₹"
                    + order.getTotalAmount()
                    + "\n"
            );
        }
    }
}