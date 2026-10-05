package com.ecommerce.ui;

import com.ecommerce.service.InventoryService;

import javax.swing.*;
import java.awt.*;

public class InventoryFrame extends JFrame {

    private final JTextArea inventoryArea;
    private final JButton refreshButton;
    private final JButton closeButton;

    private final InventoryService inventoryService;

    public InventoryFrame(InventoryService inventoryService) {

        this.inventoryService = inventoryService;

        setTitle("Inventory Management");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        inventoryArea = new JTextArea();
        inventoryArea.setEditable(false);

        refreshButton = new JButton("Refresh");
        closeButton = new JButton("Close");

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);

        setLayout(new BorderLayout());

        add(new JScrollPane(inventoryArea), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(
                e -> refreshInventory()
        );

        closeButton.addActionListener(
                e -> dispose()
        );

        refreshInventory();
    }

    private void refreshInventory() {

        inventoryArea.setText(
                "Inventory management screen.\n\n"
                + "Use InventoryService to manage stock."
        );
    }
}