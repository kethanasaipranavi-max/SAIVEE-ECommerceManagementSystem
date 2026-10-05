package com.ecommerce.ui;

import com.ecommerce.model.Product;
import com.ecommerce.service.ProductService;

import javax.swing.*;
import java.awt.*;

public class ProductManagementFrame extends JFrame {

    private final ProductService productService;

    private final JTextArea productArea;
    private final JButton refreshButton;
    private final JButton closeButton;

    public ProductManagementFrame(ProductService productService) {

        this.productService = productService;

        setTitle("Product Management");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        productArea = new JTextArea();
        productArea.setEditable(false);

        refreshButton = new JButton("Refresh");
        closeButton = new JButton("Close");

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);

        setLayout(new BorderLayout());

        add(new JScrollPane(productArea), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(
                e -> refreshProducts()
        );

        closeButton.addActionListener(
                e -> dispose()
        );

        refreshProducts();
    }

    private void refreshProducts() {

        productArea.setText("");

        if (productService == null) {

            productArea.setText(
                    "Product service is not available."
            );

            return;
        }

        for (Product product : productService.getProducts()) {

            productArea.append(
                    "----------------------------\n"
                    + "Product ID: "
                    + product.getProductId()
                    + "\n"
                    + "Name: "
                    + product.getProductName()
                    + "\n"
                    + "Category: "
                    + product.getCategory()
                    + "\n"
                    + "Price: ₹"
                    + product.getPrice()
                    + "\n"
                    + "Quantity: "
                    + product.getQuantity()
                    + "\n"
                    + "Brand: "
                    + product.getBrand()
                    + "\n"
            );
        }
    }
}