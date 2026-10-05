package com.ecommerce.ui;

import com.ecommerce.service.DeliveryService;

import javax.swing.*;
import java.awt.*;

public class DeliveryFrame extends JFrame {

    private final JTextArea deliveryArea;
    private final JButton refreshButton;
    private final JButton closeButton;

    private final DeliveryService deliveryService;

    public DeliveryFrame(DeliveryService deliveryService) {

        this.deliveryService = deliveryService;

        setTitle("Delivery Management");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        deliveryArea = new JTextArea();
        deliveryArea.setEditable(false);

        refreshButton = new JButton("Refresh");
        closeButton = new JButton("Close");

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);

        setLayout(new BorderLayout());

        add(new JScrollPane(deliveryArea), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(
                e -> refreshDeliveries()
        );

        closeButton.addActionListener(
                e -> dispose()
        );

        refreshDeliveries();
    }

    private void refreshDeliveries() {

        deliveryArea.setText(
                "Delivery management screen.\n\n"
                + "Use DeliveryService to manage deliveries."
        );
    }
}